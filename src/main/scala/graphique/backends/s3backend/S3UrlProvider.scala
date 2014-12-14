package graphique.backends.s3backend

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import graphique.backends.{RequestedImage, UrlProvider}

class S3UrlProvider(accessKey: String, secretKey: String, bucket: String, paths: S3Paths) extends UrlProvider {

  private lazy val s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey))

  def forRequestedImage(requestedImage: RequestedImage): Option[String] = {
    val path = paths ofImage requestedImage
    Option(s3Client getUrl(bucket, path.toString)) map (_.toString)
  }
}