package graphique.images

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.{IIOException, ImageIO}

case class Dimensions(width: Int, height: Int) {

  def fitsWithin(other: Dimensions): Boolean =
    this.height <= other.height && this.width <= other.width
}

object Dimensions {
  def of(bufferedImage: BufferedImage): Dimensions = Dimensions(bufferedImage.getWidth, bufferedImage.getHeight)

  /**
   * @throws IIOException
   */
  def of(image: Array[Byte]): Dimensions = of(ImageIO read new ByteArrayInputStream(image))
}
