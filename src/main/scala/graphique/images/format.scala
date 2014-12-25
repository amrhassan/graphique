
package graphique.images

sealed trait ImageFormat {
  def fileNameExtension: String
}

/**
 * The JPEG image format.
 *
 * @param quality the quality compression parameter, must be in [0, 1.0]
 */
case class JPEGFormat(quality: Double = 0.95) extends ImageFormat {
  require(quality >= 0 && quality <= 1.0)

  val fileNameExtension = ".jpg"
}

case object PNGFormat extends ImageFormat { val fileNameExtension = ".png" }
case object GIFFormat extends ImageFormat { val fileNameExtension = ".gif" }
case object BMPFormat extends ImageFormat { val fileNameExtension = ".bmp" }