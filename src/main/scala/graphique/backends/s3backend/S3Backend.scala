package graphique.backends.s3backend

import java.nio.file
import java.nio.file.Path

import com.typesafe.config.Config
import graphique.backends._

class S3Backend private(rawImages: RawImageManager, images: ImageManager, urlCache: UrlCache)
  extends Backend(rawImages, images, urlCache)

object S3Backend {

  def apply(accessKey: String, secretKey: String, bucket: String, urlCachePath: Path): S3Backend = {

    val io = new S3IO(accessKey, secretKey, bucket)
    val paths = new S3Paths

    val rawImages = new RawImageManager(paths, io)
    val requestedImageCache = new RequestedImageCache(io, paths)
    val urlProvider = new S3UrlProvider(accessKey, secretKey, bucket, paths)
    val images = new ImageManager(requestedImageCache, urlProvider)
    val urlCache: UrlCache = UrlCache.mapDb(urlCachePath)

    new S3Backend(rawImages, images, urlCache)
  }

  def apply(config: Config): S3Backend =
    apply(
      config.getString("accessKey"),
      config.getString("secretKey"),
      config.getString("bucket"),
      file.Paths.get(config.getString("urlCachePath"))
    )
}