package graphique.backends

import java.nio.file.Path

import graphique.images
import org.apache.commons.codec.digest.DigestUtils

/**
 * The single authoritative point for where files should be stored.
 */
class Paths(val rawImagePath: Path, val imagePath: Path) {

  import Paths._

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
   */
  def ofImage(requestedImage: RequestedImage): Path =
    imagePath resolve (requestedImage.tag + "-" + hashImageAttributes(requestedImage.attributes))

  /**
   * The scheme in which the given image is stored in the cache.
   *
   * The result should be interpreted as the directory path where the images are stored
   * and the filename prefix of all the files belonging to the given tag.
   */
  def imagePathScheme(imageTag: String): (Path, String) = (imagePath, s"$imageTag-")
}

object Paths {

  /**
   * Computes and returns the MD5 hexadecimal hash value of the input ImageAttributes.
   */
  def hashImageAttributes(imageAttributes: images.ImageAttributes): String =
    DigestUtils md5Hex imageAttributes.toString.toLowerCase
}