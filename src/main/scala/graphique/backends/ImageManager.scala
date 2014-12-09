package graphique.backends

import graphique.images.ImageProcessor

/**
 * The manager of processed and servable images
 */
trait ImageManager {

  def cache: RequestedImageCache

  private val imageProcessor = new ImageProcessor

  def urlScheme: UrlProvider

  /**
   * Provide a servable URL for the requested image.
   *
   * The manager keeps an internal cache of the servable images and generates one only when
   * none are found in the cache.
   *
   * @param request the requested image
   * @param rawImage the raw image to be used to generate the servable image
   * @return a URL pointing to an endpoint serving the requested image, or None if it was no available
   * @throws ImageProcessingError
   */
  def imageUrl(request: RequestedImage, rawImage: => Option[Array[Byte]]): Option[String] = {

    def processRequestedImageIntoCache: Unit = {

      rawImage foreach { rawImage =>

        val errorOrImage = imageProcessor.process(rawImage, request.attributes)

        errorOrImage match {
          case Right(processedImage) => cache.store(request, processedImage)
          case Left(error) => throw ImageProcessingError(request.tag, request.attributes, error)
        }
      }
    }

    if (!cache.has(request))
      processRequestedImageIntoCache

    urlScheme.forRequestedImage(request)
  }
}