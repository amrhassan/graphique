package graphique.localbackend

import java.nio.file.Path

/**
 * The single authoritative point for where files should be stored.
 *
 * @param storageLocation the root path of all files
 */
private[localbackend] class FilePaths(storageLocation: Path) {

  def ofRawImage(tag: String): Path = storageLocation resolve tag

  def ofThumbnail(tag: String, width: Int, height: Int): Path =
    (storageLocation resolve ("%dx%d" format(width, height))) resolve tag
}
