package graphique.images

import java.io.{IOException, ByteArrayOutputStream, ByteArrayInputStream}
import javax.imageio.IIOException

import graphique.backends.ImageContent
import net.coobird.thumbnailator.Thumbnails

/**
 * Validates image content.
 */
class ImageValidator extends (ImageContent => Boolean) {

  override def apply(image: ImageContent): Boolean = isValid(image)

  /**
   * Checks the input image and returns true if it is not corrupted, false otherwise.
   */
  def isValid(image: ImageContent): Boolean =
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