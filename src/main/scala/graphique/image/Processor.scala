package graphique.image

import java.io.{IOException, ByteArrayOutputStream, ByteArrayInputStream}

import net.coobird.thumbnailator.Thumbnails

/**
 * A processor of raw images.
 *
 * @param desiredAttributes the desired attributes of the output image
 */
class Processor(desiredAttributes: Attributes) {

  case class ProcessingError(cause: Throwable) extends RuntimeException(cause)

  /**
   * Processes the input raw image into an image with the given desired attributes.
   *
   * @param image the input image
   */
  def process(image: Array[Byte]): Either[ProcessingError, Array[Byte]] = {

    val inputStream = new ByteArrayInputStream(image)
    val thumbnailator = Thumbnails.of(inputStream)

    val sizedThumbnailator = desiredAttributes.size match {
      case Attribute.OriginalSize => thumbnailator
      case Attribute.SizeWithin(dimensions) => thumbnailator.size(dimensions.width, dimensions.height)
    }

    val formattedThumbnailator = desiredAttributes.format match {
      case Attribute.OriginalFormat => thumbnailator
      case Attribute.TranscodedFormat(format) => format match {
        case PNGFormat => thumbnailator.outputFormat("PNG")
        case JPEGFormat(quality) => thumbnailator.outputFormat("JPEG").outputQuality(quality)
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
