package graphique.localbackend

import java.nio.file.Path

/**
 * A local backend implementation that stores the image files on the local filesystem and serves the images
 * via an embedded HTTP server.
 *
 * @param storageLocation the local image storage location
 * @param httpPort the local image serving HTTP port
 */
class LocalBackendModule(storageLocation: Path, httpPort: Int) extends LocalBackend {

  def io: IO = new IO

  def filePaths: FilePaths = new FilePaths(storageLocation)

  def imageServer: ImageServer = new ImageServer(httpPort)

}
