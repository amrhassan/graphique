package graphique.backends.s3backend

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import graphique.backends.{ImageTag, ImageId, RequestedImage, UrlProvider}

class S3UrlProvider(accessKey: String, secretKey: String, bucket: String, paths: S3Paths) extends UrlProvider {

  private lazy val s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey))

  def forImage(id: ImageId): String = {
    val s3Path = paths ofImage id
    (s3Client getUrl(bucket, s3Path.toString)).toString
  }
}