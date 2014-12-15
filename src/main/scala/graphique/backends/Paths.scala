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
  def ofRawImage(tag: ImageTag): Path = rawImagePath resolve tag

  /**
   * The path to the image.
   *
   * This is where the raw image after processing should be cached to.
   */
  def ofImage(id: ImageId): Path = imagePath resolve id

  private val possibleImageFileNameExtensions = Set(".jpg", ".png")

  def possibleImageIds(requestedImage: RequestedImage): Set[ImageId] =
    for (extension <- possibleImageFileNameExtensions)
      yield s"${requestedImage.tag}-${hashImageAttributes(requestedImage.attributes)}$extension"

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