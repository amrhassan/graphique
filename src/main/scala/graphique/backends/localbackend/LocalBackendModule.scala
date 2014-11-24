package graphique.backends.localbackend

import java.nio.file.Path

import graphique.backends.abstractbackend.{RawImageManager, ImageManager, URLProvider, AbstractBackend}
import graphique.image.ImageProcessor

/**
 * A local backend implementation that stores the image files on the local filesystem and serves the images
 * via an embedded HTTP server.
 *
 * @param storageLocation the local image storage location
 * @param httpPort the local image serving HTTP port
 */
class LocalBackendModule(storageLocation: Path, httpPort: Int) extends AbstractBackend {

//  def io: IO = new IO
//
//  def filePaths: FilePaths = new FilePaths(storageLocation)
//
//  def imageServer: ImageServer = new ImageServer(httpPort)

  private lazy val filePaths = new FilePaths(storageLocation)

  private lazy val io = new IO

  override protected def rawImages: RawImageManager = new LocalRawImageManager(filePaths, io)

  override protected def images: ImageManager = new LocalImageManager(filePaths, io)

  override protected def imageProcessor: ImageProcessor = ???

  override protected def urls: URLProvider = ???
}
