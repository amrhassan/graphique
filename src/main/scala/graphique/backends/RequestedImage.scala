package graphique.backends

import graphique.images
import graphique.images.ImageAttribute.{TranscodedFormat, OriginalFormat}

/**
 * The definition of a requested image.
 *
 * @param tag the identifier tag of the requested image
 * @param attributes the attributes of the requested image
 */
case class RequestedImage(tag: String, attributes: images.ImageAttributes) {

  /**
   * The file name extension for the requested image.
   *
   * This is only available when the requested image has an image format specified.
   */
  lazy val fileNameExtension: Option[String] = {
    attributes.format match {
      case OriginalFormat => None
      case TranscodedFormat(imageFormat) => Some(imageFormat.fileNameExtension)
    }
  }
}
