package graphique.backends

import graphique.images.ImageAttributes

/**
 * A Graphique backend.
 *
 */
class Backend(images: ImageManager, urls: UrlProvider) {

  /**
   * Submits a new image.
   *
   * @param image the image content
   * @throws InvalidImageError when the submitted content is not a valid image
   * @throws IOError
   * @return the tag of the submitted image
   */
  def submitImage(image: Array[Byte]): ImageTag = images submit image

  /**
   * Requests that an image matching the specified attributes should be created.
   *
   * @param tag the image identifier
   * @param attributes the attributes of the requested image
   * @throws IOError
   * @throws ImageProcessingError
   * @throws SourceImageNotFoundError when the image requested is unavailable
   */
  def createImage(tag: ImageTag, attributes: ImageAttributes): Unit =
    images createImage Image(tag, attributes)

  def imageAvailable(tag: ImageTag, attributes: ImageAttributes): Boolean = images has Image(tag, attributes)

  /**
   * Makes sure that the requested image exists (or creates it) then returns its URL.
   *
   * @param tag
   * @param attributes
   * @throws ImageProcessingError
   * @throws SourceImageNotFoundError when the image requested is unavailable
   */
  def urlForExistingImage(tag: ImageTag, attributes: ImageAttributes): String = {
    if (!imageAvailable(tag, attributes))
      createImage(tag, attributes)
    imageUrlFor(tag, attributes)
  }

  /**
   * Request a publicly-servable URL for the image identified by the given tag and is with the given
   * attributes. The image is only available if this was preceded by a call to createImage() with the
   * same arguments.
   *
   * @param tag the identifier of the requested image
   * @param attributes the desired attributes of the requested image
   */
  def imageUrlFor(tag: ImageTag, attributes: ImageAttributes): String = {
    urls(Image(tag, attributes).id)
  }
}
