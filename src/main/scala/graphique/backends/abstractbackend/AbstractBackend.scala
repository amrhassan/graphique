package graphique.backends.abstractbackend

import graphique.backends.{ImageProcessingError, ImageSubmissionError, Backend}
import graphique.image

/**
 * An abstract implementation of a Backend. Subclass and provide concrete implementations for
 * the abstract members and you're good to go!
 */
trait AbstractBackend extends Backend {

  /**
   * The manager of raw images.
   */
  protected def rawImages: RawImageManager

  /**
   * The manager of processed images.
   */
  protected def images: ImageManager

  /**
   * The processor of raw images into processed images.
   */
  protected def imageProcessor: image.Processor

  /**
   * The provider of servable image URLs.
   */
  protected def urls: URLProvider

  override final def submit(image: Array[Byte], tag: String): Unit = {

    // TODO: Log this call

    try {
      images.clearImageCaches(tag)
      rawImages.store(tag, image)
    } catch {
      case e: IOError => throw new ImageSubmissionError("Failed to submit " + tag, e)
    }
  }

  override final def imageUrl(tag: String, attributes: image.Attributes): Option[String] = {

    // TODO: Log this call

    val requestedImage = RequestedImage(tag, attributes)

    def processRequestedImageIntoCache: Unit = {

      val rawImage = rawImages.read(tag)

      if (rawImage.isEmpty)
        // TODO: Log about requesting an image that doesn't exist

      rawImage foreach { rawImage =>

        val errorOrImage = imageProcessor.process(rawImage)

        errorOrImage match {
          case Right(processedImage) => images.cacheImage(requestedImage, processedImage)
          case Left(error) => throw ImageProcessingError(tag, attributes, error)
        }
      }
    }

    if (!images.isImageCached(requestedImage))
      processRequestedImageIntoCache

    urls.forRequestedImage(requestedImage)
  }
}
