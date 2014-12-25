package graphique.images

import java.io.{IOException, ByteArrayOutputStream, ByteArrayInputStream}
import javax.imageio.ImageIO

import net.coobird.thumbnailator.Thumbnails

/**
 * A processor of raw images.
 */
class ImageProcessor {

  case class ProcessingError(cause: Throwable) extends RuntimeException(cause)

  /**
   * Processes the input raw image into an image with the given desired attributes.
   *
   * @param image the input image
   * @param desiredAttributes the desired attributes of the output image
   */
  def process(image: Array[Byte], desiredAttributes: ImageAttributes): Either[ProcessingError, Array[Byte]] = {

    val originalDimensions = Dimensions of image
    val thumbnailator = Thumbnails of new ByteArrayInputStream(image)

    val sizedThumbnailator = desiredAttributes.size match {
      case ImageAttribute.OriginalSize => thumbnailator.scale(1.0)
      case ImageAttribute.SizeWithin(dimensions) =>
        if (dimensions fitsWithin originalDimensions)
          thumbnailator.size(dimensions.width, dimensions.height)
        else
          thumbnailator.scale(1.0)
    }

    val formattedThumbnailator = desiredAttributes.format match {
      case ImageAttribute.OriginalFormat => thumbnailator
      case ImageAttribute.TranscodedFormat(format) => format match {
        case PNGFormat => thumbnailator.outputFormat("PNG")
        case JPEGFormat(quality) => thumbnailator.outputFormat("JPEG").outputQuality(quality)
        case BMPFormat => thumbnailator.outputFormat("BMP")
        case GIFFormat => thumbnailator.outputFormat("GIF")
      }
    }

    val finalThumbnailator = formattedThumbnailator
    val outputStream = new ByteArrayOutputStream

    try {
      finalThumbnailator.toOutputStream(outputStream)
    } catch {
      case e: IOException => return Left(ProcessingError(e))
    }

    Right(outputStream.toByteArray)
  }
}
