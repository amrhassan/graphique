package graphique.backends

import graphique.images
import graphique.images.ImageProcessor

/**
 * The manager of processed and servable images
 */
class ImageManager(val cache: RequestedImageCache, val urlProvider: UrlProvider) {

  private val imageProcessor = new ImageProcessor

  case class ImageProcessingError(imageTag: String, desiredAttributes: images.ImageAttributes, cause: Throwable)
    extends RuntimeException(s"Failed while processing the image for $imageTag: $desiredAttributes", cause)

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

    def processRequestedImageIntoCache: Option[ImageId] = {
      rawImage map { rawImageBytes =>
        val errorOrImage = imageProcessor.process(rawImageBytes, request.attributes)
        errorOrImage match {
          case Right(processedImage) => {
            val fileNameExtension = (Content detectFileNameExtension processedImage).getOrElse("")
            val imageId = s"${request.tag}-${Paths hashImageAttributes request.attributes}$fileNameExtension"
            cache.store(imageId, processedImage)
            imageId
          }
          case Left(error) => throw ImageProcessingError(request.tag, request.attributes, error)
        }
      }
    }

    val availableImages = cache.availableImages(request)
    if (!availableImages.isEmpty)
      Some(urlProvider forImage availableImages.head)
    else
      processRequestedImageIntoCache map urlProvider.forImage
  }
}