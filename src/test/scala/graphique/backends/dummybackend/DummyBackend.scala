package graphique.backends.dummybackend

import graphique.backends.{RequestedImageCache, ImageManager, RawImageManager, Backend}

/**
 * A fully functional backend that stores the file content in memory.
 */
class DummyBackend private(rawImages: RawImageManager, images: ImageManager)
  extends Backend(rawImages, images)

object DummyBackend {

  def apply(): DummyBackend = {
    val requestedImageCache = new RequestedImageCache(DummyIO, DummyPaths)
    val rawImages = new RawImageManager(DummyPaths, DummyIO)
    val images = new ImageManager(requestedImageCache, DummyUrlProvider)

    new DummyBackend(rawImages, images)
  }
}