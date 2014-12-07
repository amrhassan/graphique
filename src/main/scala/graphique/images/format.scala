
package graphique.images

sealed trait ImageFormat

/**
 * The JPEG image format.
 *
 * @param quality the quality compression parameter, must be in [0, 1.0]
 */
case class JPEGFormat(quality: Double) extends ImageFormat {
  require(quality >= 0 && quality <= 1.0)
}

/**
 * The PNG image format.
 */
case object PNGFormat extends ImageFormat