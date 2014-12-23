package graphique.backends

import graphique.images
import graphique.images._
import org.apache.commons.codec.digest.DigestUtils

/**
 * The definition of an image.
 *
 * @param tag the identifier tag
 * @param attributes the attributes of the image
 */
case class Image(tag: String, attributes: images.ImageAttributes) {

  private lazy val FileNameExtensionPattern = """[^\.]*(\..*)$""".r

  lazy val fileNameExtension: String = tag match {
    case FileNameExtensionPattern(extension) => extension
  }

  private lazy val extensionlessTag: String =
    tag substring (0, tag lastIndexOf '.')

  lazy val id: ImageId =
    if (attributes.isUnmodified)
      tag
    else
      s"$extensionlessTag-$hashedAttributes$fileNameExtension"

  lazy val sourceId: ImageId = tag

  /**
   * A MD5 hexadecimal hash value of the attributes.
   */
  lazy val hashedAttributes: String = {

    /**
     * The hashed attributes value is made up of the following:
     *
     *    if (formatAttribute.isEmpty && sizeAttribute.isEmpty)
     *      ""
     *    else
     *      md5Hex(concat(formatAttribute, sizeAttribute))
     *
     * Where:
     *
     *    md5Hex() is a function which computes the MD5 checksum as a character string of hexadecimal bytes
     *      of a character string input.
     *
     *    concat() produces the concatenation of its two input character strings as a character string.
     *
     *    formatAttribute is:
     *      - an empty character string when the original format is requested
     *      - "JPEG(quality)" when the JPEG format is requested, where quality is a real value between
     *        0 and 1.0 indicating the JPEG output quality
     *      - "PNG" when the PNG format is requested
     *    sizeAttribute is:
     *      - an empty character string when the original size is requested
     *      - "size-within=WIDTHxHEIGHT" when the requested size needs to fit within the values of WIDTH
     *        and HEIGHT in pixels.
     */

    val sizeAttribute: String = attributes.size match {
      case ImageAttribute.OriginalSize => ""
      case ImageAttribute.SizeWithin(dimensions) => s"size-within=${dimensions.width}x${dimensions.height}"
    }

    val formatAttribute: String = attributes.format match {
      case ImageAttribute.OriginalFormat => ""
      case ImageAttribute.TranscodedFormat(imageFormat) =>
        imageFormat match {
          case PNGFormat => "PNG"
          case JPEGFormat(quality) => s"JPEG($quality)"
        }
    }

    if (formatAttribute.isEmpty && sizeAttribute.isEmpty)
      ""
    else
      DigestUtils md5Hex s"$formatAttribute$sizeAttribute"
  }
}

object Image {

  private val imageValidator = new ImageValidator

  /**
   * Creates an Image instance based on actual image content.
   *
   * @throws InvalidImageError when the input bytes do not belong to an image
   */
  def from(imageContent: ImageContent): Image = {

    /**
     * Returns an image tag for the given image.
     *
     * The image tag must be end with an appropriate file name extension for the image type.
     */
    def tagFor(imageContent: ImageContent): ImageTag = {
      val extension = (Content detectFileNameExtension imageContent) getOrElse (throw new InvalidImageError)
      val imageHash = DigestUtils md5Hex imageContent
      s"$imageHash$extension"
    }

    if (!imageValidator(imageContent))
      throw new InvalidImageError
    else {
      Image(tagFor(imageContent), ImageAttributes.originalImage)
    }
  }
}
