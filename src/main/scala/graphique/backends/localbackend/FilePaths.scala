package graphique.backends.localbackend

import java.nio.file.Path
import graphique.backends.Paths

/**
 * The single authoritative point for where files should be stored, in the local backend.
 *
 * @param storageLocation the root path of all files
 */
private[localbackend] class FilePaths(storageLocation: Path) extends Paths(
    rawImagePath = storageLocation resolve "raw",
    imagePath = storageLocation resolve "image") {

  /**
   * The local path for the file corresponding to the requested ID coming through HTTP.
   *
   * @param identifier the ID coming in and in need to be matched to a filesystem path
   */
  def ofUrlComponent(identifier: String): Path = imagePath resolve identifier
}
