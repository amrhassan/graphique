package graphique.backends.s3backend

import graphique.backends.{Image, ImageId, UrlProvider}


class S3UrlProvider(bucket: String, paths: S3Paths) extends UrlProvider {
  def apply(id: ImageId): String =
    s"https://$bucket.s3.amazonaws.com/${paths ofImage id}}"
}