package graphique.backends.s3backend

import java.io.ByteArrayInputStream
import java.nio.file.Path
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.{AmazonS3, AmazonS3Client}
import com.amazonaws.services.s3.model.{PutObjectRequest, CannedAccessControlList, ObjectMetadata}
import com.amazonaws.services.s3.transfer.TransferManager
import com.typesafe.scalalogging.LazyLogging
import graphique.backends.{Content, IO}
import scala.io.{Codec, Source}

/**
 * Low-level AWS S3 IO.
 */
private[s3backend] class S3IO(accessKey: String, secretKey: String, bucket: String) extends IO with LazyLogging {

  lazy val s3Client: AmazonS3 = this.synchronized {
    new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey))
  }

  def write(dest: Path)(data: Array[Byte]): Unit = {
    logger debug "WRITE"
    val request = new PutObjectRequest(bucket, dest.toString, new ByteArrayInputStream(data), metadataFor(data))
    request.getRequestClientOptions.setReadLimit(data.length)
    request.setCannedAcl(CannedAccessControlList.PublicRead)
    s3Client.putObject(request)
  }

  private def metadataFor(data: Array[Byte]): ObjectMetadata = {
    val metadata = new ObjectMetadata

    // Try to detect the mime type
    (Content detectMimeType data) foreach (mimeType => metadata setContentType mimeType)

    metadata
  }

  def delete(directory: Path, prefix: String): Unit = {
    logger debug "DELETE"
    import scala.collection.JavaConversions._
    val files = s3Client listObjects(bucket, prefix)
    files.getObjectSummaries foreach { objectSummary =>
      s3Client deleteObject(bucket, objectSummary.getKey)
    }
  }

  def exists(path: Path): Boolean = {
    logger debug "EXISTS"
    try {
      val s3Object = Option(s3Client getObject(bucket, path.toString))
      s3Object foreach (_.close())

      s3Object.isDefined
    } catch {
      case e: AmazonServiceException => if (e.getErrorCode == "NoSuchKey") false else throw e
    }
  }

  def read(path: Path): Array[Byte] = {
    logger debug "READ"
    val s3Object = s3Client.getObject(bucket, path.toString)
    val inStream = s3Object.getObjectContent
    val source = Source.fromInputStream(inStream)(Codec.ISO8859)
    val data = (source map (_.toByte)).toArray
    inStream.close()
    s3Object.close()
    data
  }
}