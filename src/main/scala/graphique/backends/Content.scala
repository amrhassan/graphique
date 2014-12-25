package graphique.backends

import graphique.images._
import net.sf.jmimemagic.{MagicException, MagicMatchNotFoundException, MagicParseException, Magic}

/**
 * Operations on binary content.
 */
object Content {

  /**
   * Detects the mime type of data, if possible.
   */
  def detectMimeType(data: Array[Byte]): Option[String] =
    try {
      Some(Magic.getMagicMatch(data).getMimeType)
    } catch {
      case _: MagicParseException => None
      case _: MagicMatchNotFoundException => None
      case _: MagicException => None
      case _: Throwable => None   // The JMimeMagic library is old and throws weird errors sometimes
    }

  /**
   * Detects and returns the image format of the given image content.
   *
   * @return the image format or None of it's not a recognizable image
   */
  def detectImageFormat(data: ImageContent): Option[ImageFormat] = {
    detectMimeType(data) flatMap {
      case "image/jpeg" => Some(JPEGFormat())
      case "image/jpg" => Some(JPEGFormat())
      case "image/png" => Some(PNGFormat)
      case "image/gif" => Some(GIFFormat)
      case "image/bmp" => Some(BMPFormat)
      case _ => None
    }
  }
}