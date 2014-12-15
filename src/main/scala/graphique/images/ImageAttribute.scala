package graphique.images

/**
 * A single attribute of an image.
 *
 * Houses the different possible attributes of an image.
 *
 * The hierarchy of the attribute values goes as follows:
 *
 * Attribute -|
 *            |
 *            |- Size -|
 *            |        |- OriginalSize
              |        |- SizeWithin(dimensions)
 *            |
 *            |
 *            |- Format -|
 *                       |- OriginalFormat
 *                       |- TranscodedFormat(imageFormat)
 */
class ImageAttribute


object ImageAttribute {

  /**
   * The output format of an image.
   */
  sealed trait Format extends ImageAttribute

  /**
   * The output size of an image.
   */
  sealed trait Size extends ImageAttribute

  /**
   * The image size should fit within the given dimensions.
   *
   * This attribute would usually be accomplished by performing a resize operation that
   * preserves the original aspect ratio of the input image.
   */
  case class SizeWithin(dimensions: Dimensions) extends Size

  /**
   * The image should bear the original input size.
   */
  case object OriginalSize extends Size

  /**
   * The image should bear the original input format.
   */
  case object OriginalFormat extends Format

  /**
   * The image should be transcoded to the given format.
   */
  case class TranscodedFormat(imageFormat: ImageFormat) extends Format
}
