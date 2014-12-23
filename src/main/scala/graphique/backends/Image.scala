package graphique.backends

import graphique.images
import graphique.images.{ImageValidator, ImageAttributes}
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
  lazy val hashedAttributes: String = DigestUtils md5Hex attributes.toString.toLowerCase
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
