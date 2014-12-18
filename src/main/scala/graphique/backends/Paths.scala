package graphique.backends

import java.nio.file.Path

import graphique.images
import org.apache.commons.codec.digest.DigestUtils

/**
 * The single authoritative point for where files should be stored.
 */
class Paths(val pathPrefix: Path) {

  /**
   * The path to the image.
   */
  def ofImage(id: ImageId): Path = pathPrefix resolve id

  /**
   * The scheme in which the given image is stored in the cache.
   *
   * The result should be interpreted as the directory path where the images are stored
   * and the filename prefix of all the files belonging to the given tag.
   */
  def imagePathScheme(imageTag: String): (Path, String) = (pathPrefix, s"$imageTag-")
}
