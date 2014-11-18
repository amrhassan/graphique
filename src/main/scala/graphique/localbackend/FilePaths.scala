package graphique.localbackend

import java.nio.file.Path

import graphique.Dimensions

/**
 * The single authoritative point for where files should be stored.
 *
 * @param storageLocation the root path of all files
 */
private[localbackend] class FilePaths(storageLocation: Path) {

  /**
   * All the paths to processed images must end in this suffix.
   */
  private val FullSizeSuffix = ".jpg"

  def ofRawImage(tag: String): Path = (storageLocation resolve "raw") resolve tag

  def ofFullSizeImage(tag: String): Path = {
    val parent = storageLocation resolve "fullsize"
    parent resolve (tag + FullSizeSuffix)
  }

  def ofThumbnailImage(tag: String, dimensions: Dimensions): Path = {
    val parent = storageLocation resolve "thumbnail"
    (parent resolve dimensions.canonicalString) resolve (tag + FullSizeSuffix)
  }
}
