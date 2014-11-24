package graphique.backends.localbackend

import java.nio.file.Path
import org.apache.commons.codec.digest.DigestUtils
import graphique.image

/**
 * The single authoritative point for where files should be stored.
 *
 * @param storageLocation the root path of all files
 */
private[localbackend] class FilePaths(storageLocation: Path) {

  private val rawImagePath = storageLocation resolve "raw"
  private val imagePath = storageLocation resolve "image"

  import FilePaths._

  /**
   * The path to the raw image submitted initially.
   *
   * @param tag the identifier tag of the image
   */
  def ofRawImage(tag: String): Path = rawImagePath resolve tag

  /**
   * The path to the image.
   *
   * This is where the raw image after processing should be cached to.
   *
   * @param tag the identifier of the image
   * @param attributes the attributes of the requested image
   */
  def ofImage(tag: String, attributes: image.Attributes): Path =
    imagePath resolve (tag + "-" + hashImageAttributes(attributes))
}

private[localbackend] object FilePaths {

  /**
   * Computes and returns the MD5 hexadecimal hash value of the input ImageAttributes.
   */
  def hashImageAttributes(imageAttributes: image.Attributes): String =
    DigestUtils md5Hex imageAttributes.toString.toLowerCase
}
