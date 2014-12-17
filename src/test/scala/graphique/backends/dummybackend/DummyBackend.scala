package graphique.backends.dummybackend

import graphique.backends._

/**
 * A fully functional backend that stores the file content in memory.
 */
class DummyBackend private(rawImages: RawImageManager, images: ImageManager)
  extends Backend(rawImages, images, UrlCache.noCache)

object DummyBackend {

  def apply(): DummyBackend = {
    val io = new DummyIO
    val urls = new DummyUrlProvider(io, DummyPaths)
    val requestedImageCache = new RequestedImageCache(io, DummyPaths)
    val rawImages = new RawImageManager(DummyPaths, io)
    val images = new ImageManager(requestedImageCache, urls)

    new DummyBackend(rawImages, images)
  }
}