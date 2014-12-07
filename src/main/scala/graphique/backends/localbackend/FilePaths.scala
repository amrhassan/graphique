package graphique.backends.localbackend

import java.nio.file.Path
import graphique.backends.RequestedImage
import org.apache.commons.codec.digest.DigestUtils
import graphique.images

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
   */
  def ofImage(requestedImage: RequestedImage): Path =
    imagePath resolve (requestedImage.tag + "-" + hashImageAttributes(requestedImage.attributes))

  /**
   * The local path for the file corresponding to the requested ID coming through HTTP.
   *
   * @param identifier the ID coming in and in need to be matched to a filesystem path
   */
  def ofUrlComponent(identifier: String): Path = imagePath resolve identifier

  /**
   * The scheme in which the given image is stored in the cache.
   *
   * The result should be interpreted as (directoryPath, globMatchingAllImageInstances).
   *
   * Example:
   *  For the image tagged "aj42", the response should be something like:
   *    (Path("/images_path"), "aj42-*")
   *
   *
   * This is a very specific thing to require, though it is needed in some other part of the
   * system and I would rather to have all the image path related logic to be contained here.
   */
  def imagePathScheme(imageTag: String): (Path, String) = (imagePath, s"$imageTag-*")

}

private[localbackend] object FilePaths {

  /**
   * Computes and returns the MD5 hexadecimal hash value of the input ImageAttributes.
   */
  def hashImageAttributes(imageAttributes: images.ImageAttributes): String =
    DigestUtils md5Hex imageAttributes.toString.toLowerCase
}
