package graphique.service

import java.nio.file.Path

import graphique.backends.localbackend._
import graphique.backends.{ImageManager, RawImageManager}

/**
 * A locally-backed ImageService implementation.
 *
 * This implementation stores its image files on the local filesystem, and serves its images itself
 * over HTTP.
 *
 * To create an instance, use the factory method in the LocalImageService object.
 */
class LocalImageService private(rawImages: RawImageManager, images: ImageManager)
  extends ImageService(rawImages, images)

object LocalImageService {

  /**
   * A factory method for a LocalImageService.
   *
   * @param storageLocation the location where the submitted images are stored
   * @param httpPort the port on which the internal HTTP server will listen to
   */
  def apply(storageLocation: Path, httpPort: Int): ImageService = {
    require(httpPort > 0, "httpPort must be a non negative number")

    val filePaths = new FilePaths(storageLocation)
    val io = new LocalIO

    val requestedImageCache = new LocalRequestedImageCache(filePaths, io)
    val urlProvider = new LocalUrlProvider(httpPort, filePaths, io)

    val rawImages = new LocalRawImageManager(filePaths, io)
    val images = new LocalImageManager(requestedImageCache, urlProvider)

    new LocalImageService(rawImages, images)
  }
}