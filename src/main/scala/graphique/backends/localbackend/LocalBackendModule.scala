package graphique.backends.localbackend

import java.nio.file.Path

import graphique.backends.abstractbackend._
import graphique.image.ImageProcessor

/**
 * A local backend implementation that stores the image files on the local filesystem and serves the images
 * via an embedded HTTP server.
 *
 * @param storageLocation the local image storage location
 * @param httpPort the local image serving HTTP port
 */
class LocalBackendModule(storageLocation: Path, httpPort: Int) extends AbstractBackend {

  private lazy val filePaths = new FilePaths(storageLocation)

  private lazy val io = new LocalIO

  override protected lazy val rawImages: RawImageManager = new LocalRawImageManager(filePaths, io)

  override protected def images: ImageManager =
    new LocalImageManager(cache = new LocalRequestedImageCache(filePaths, io),
                           urlScheme = new ImageServer(httpPort, filePaths, io),
                           imageProcessor = new ImageProcessor)

}
