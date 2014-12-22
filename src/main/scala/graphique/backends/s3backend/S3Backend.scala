package graphique.backends.s3backend

import com.typesafe.config.Config
import graphique.backends._

class S3Backend private(images: ImageManager, urls: UrlProvider) extends Backend(images, urls)

object S3Backend {

  def apply(accessKey: String, secretKey: String, bucket: String, pathPrefix: String): S3Backend = {

    val io = new S3IO(accessKey, secretKey, bucket)
    val paths = new S3Paths(pathPrefix)
    val urlProvider = new S3UrlProvider(bucket, paths)
    val images = new ImageManager(io, paths)

    new S3Backend(images, urlProvider)
  }

  def apply(config: Config): S3Backend =
    apply(
      config.getString("accessKey"),
      config.getString("secretKey"),
      config.getString("bucket"),
      config.getString("pathPrefix")
    )
}