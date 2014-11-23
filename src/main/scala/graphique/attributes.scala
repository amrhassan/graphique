
/**
 * Houses the different possible attributes of an image.
 *
 * The hierarchy of the attribute values goes as follows:
 *
 * ImageAttribute -|
 *                 |
 *                 |- Size -|
 *                 |        |- OriginalSize
 *                 |        |- SizeWithin(dimensions)
 *                 |
 *                 |
 *                 |- Format -|
 *                            |- OriginalFormat
 *                            |- JPEGFormat(quality)
 *                            |- PNGFormat
 */
package graphique.attributes

/**
 * Attributes of an image. 
 */
sealed trait ImageAttribute

/**
 * The output size of an image.
 */
sealed trait Size extends ImageAttribute

/**
 * The output format of an image.
 */
sealed trait Format extends ImageAttribute

/**
 * The image size should fit within the given dimensions.
 *
 * This attribute would usually be accomplished by performing a resize operation that
 * preserves the original aspect ratio of the input image.
 */
case class SizeWithin(width: Int, height: Int) extends Size

/**
 * The image should bear the original input size.
 */
case object OriginalSize extends Size

/**
 * The JPEG image format.
 *
 * @param quality the quality compression parameter, must be in [0, 1.0]
 */
case class JPEGFormat(quality: Double) extends Format {
  require(quality >= 0 && quality <= 1.0)
}

/**
 * The PNG image format.
 */
case object PNGFormat extends Format

/**
 * The image should bear the original input format.
 */
case object OriginalFormat extends Format