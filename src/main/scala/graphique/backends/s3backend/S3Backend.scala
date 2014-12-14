package graphique.backends.s3backend

import graphique.backends.{RequestedImageCache, Backend, ImageManager, RawImageManager}

class S3Backend private(rawImages: RawImageManager, images: ImageManager)
  extends Backend(rawImages, images)

object S3Backend {

  def apply(accessKey: String, secretKey: String, bucket: String): S3Backend = {

    val io = new S3IO(accessKey, secretKey, bucket)
    val paths = new S3Paths

    val rawImages = new RawImageManager(paths, io)
    val requestedImageCache = new RequestedImageCache(io, paths)
    val urlProvider = new S3UrlProvider(accessKey, secretKey, bucket, paths)
    val images = new ImageManager(requestedImageCache, urlProvider)

    new S3Backend(rawImages, images)
  }
}