package graphique.backends.s3backend

import java.io.ByteArrayInputStream
import java.nio.file.Path
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.{PutObjectRequest, CannedAccessControlList, ObjectMetadata}
import com.typesafe.scalalogging.LazyLogging
import graphique.backends.{Content, IO}
import scala.io.{Codec, Source}

/**
 * Low-level AWS S3 IO.
 */
private[s3backend] class S3IO(accessKey: String, secretKey: String, bucket: String) extends IO with LazyLogging {

  def withS3Client[T](fun: AmazonS3Client => T) = {
    val client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey))
    val returned = fun(client)
    client.shutdown()
    returned
  }

  def write(dest: Path)(data: Array[Byte]): Unit = {
    logger debug "WRITE"
    withS3Client { client =>
      val request = new PutObjectRequest(bucket, dest.toString, new ByteArrayInputStream(data), metadataFor(data))
      request.setCannedAcl(CannedAccessControlList.PublicRead)
      client.putObject(request)
    }
  }

  private def metadataFor(data: Array[Byte]): ObjectMetadata = {
    val metadata = new ObjectMetadata

    // Try to detect the mime type
    (Content detectMimeType data) foreach (mimeType => metadata setContentType(mimeType))

    metadata
  }

  def delete(directory: Path, prefix: String): Unit = {
    logger debug "DELETE"
    withS3Client { s3Client =>
      import scala.collection.JavaConversions._
      val files = s3Client listObjects(bucket, prefix)
      files.getObjectSummaries foreach { objectSummary =>
        s3Client deleteObject(bucket, objectSummary.getKey)
      }
    }
  }

  def exists(path: Path): Boolean = {
    logger debug "EXISTS"
    withS3Client { s3Client =>
      try {
        Option(s3Client getObject(bucket, path.toString)).isDefined
      } catch {
        case e: AmazonServiceException => if (e.getErrorCode == "NoSuchKey") false else throw e
      }
    }
  }

  def read(path: Path): Array[Byte] = {
    logger debug "READ"
    withS3Client { client =>
      val s3Object = client.getObject(bucket, path.toString)
      val inStream = s3Object.getObjectContent
      val source = Source.fromInputStream(inStream)(Codec.ISO8859)
      val data = (source map (_.toByte)).toArray
      inStream.close()
      s3Object.close()
      data
    }
  }
}