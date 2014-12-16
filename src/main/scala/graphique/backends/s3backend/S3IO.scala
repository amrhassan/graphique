package graphique.backends.s3backend

import java.io.{IOError, ByteArrayInputStream}
import java.nio.file.Path
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.{PutObjectRequest, CannedAccessControlList, ObjectMetadata}
import graphique.backends.{Content, IO}
import scala.io.{Codec, Source}
import scala.util.Try

/**
 * Low-level AWS S3 IO.
 */
private[s3backend] class S3IO(accessKey: String, secretKey: String, bucket: String) extends IO {

  private lazy val s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey))

  def write(dest: Path)(data: Array[Byte]): Unit = {
    val request = new PutObjectRequest(bucket, dest.toString, new ByteArrayInputStream(data), metadataFor(data))
    request.setCannedAcl(CannedAccessControlList.PublicRead)
    s3Client.putObject(request)
  }

  private def metadataFor(data: Array[Byte]): ObjectMetadata = {
    val metadata = new ObjectMetadata

    // Try to detect the mime type
    (Content detectMimeType data) foreach (mimeType => metadata setContentType(mimeType))

    metadata
  }

  def delete(directory: Path, prefix: String): Unit = {
    import scala.collection.JavaConversions._
    val files = s3Client listObjects(bucket, prefix)
    files.getObjectSummaries foreach { objectSummary =>
      s3Client deleteObject(bucket, objectSummary.getKey)
    }
  }

  def exists(path: Path): Boolean =
    try {
      Option(s3Client getObject(bucket, path.toString)).isDefined
    } catch {
      case e: AmazonServiceException => if (e.getErrorCode == "NoSuchKey") false else throw e
    }

  def read(path: Path): Array[Byte] = {
    val s3Object = s3Client getObject(bucket, path.toString)
    val source = Source.fromInputStream(s3Object.getObjectContent)(Codec.ISO8859)
    (source map (_.toByte)).toArray
  }
}