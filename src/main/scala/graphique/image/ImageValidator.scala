package graphique.image

import java.io.{IOException, ByteArrayOutputStream, ByteArrayInputStream}
import javax.imageio.IIOException

import net.coobird.thumbnailator.Thumbnails

/**
 * Validates image content.
 */
class ImageValidator extends (Array[Byte] => Boolean) {

  override def apply(image: Array[Byte]): Boolean = isValid(image)

  /**
   * Checks the input image and returns true if it is not corrupted, false otherwise.
   */
  def isValid(image: Array[Byte]): Boolean =
    try {
      Thumbnails.of(new ByteArrayInputStream(image)).scale(0.2).outputFormat("JPEG")
        .toOutputStream(new ByteArrayOutputStream())
      true
    } catch {
      case _: IIOException => false
      case _: IllegalArgumentException => false
      case _: IOException => false
    }
}