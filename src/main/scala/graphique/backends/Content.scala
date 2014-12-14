package graphique.backends

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
    }
}