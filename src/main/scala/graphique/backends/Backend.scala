package graphique.backends

import graphique.images.{ImageValidator, ImageAttributes}

class Backend(rawImages: RawImageManager, images: ImageManager) {

  import Backend._

  private lazy val imageValidator = new ImageValidator

  /**
   * Submits an image to the backend, overwriting any previously submitted image with the same
   * tag if existed.
   *
   * @param tag an identifier tag for the submitted image
   * @param image the image content
   * @throws InvalidImageError when the submitted content is not a valid image
   * @throws IOError
   */
  def submitImage(tag: String, image: Array[Byte]): Unit = {

    if (imageValidator isValid image) {
      images.cache clearTaggedWith tag
      rawImages store(tag, image)
    } else {
      throw new InvalidImageError
    }
  }

  /**
   * Request a publicly-servable URL for the image identified by the given tag and is with the given
   * attributes.
   *
   * @param tag the identifier of the requested image
   * @param attributes the desired attributes of the requested image
   * @return Optionally a publicly-servable URL for the requested image, or None if the image is not
   *         available.
   * @throws IOError
   */
  def imageUrlFor(tag: String, attributes: ImageAttributes): Option[String] = {
    images imageUrl(RequestedImage(tag, attributes), rawImages read tag)
  }

  def imageUrlFor(tag: String): Option[String] = imageUrlFor(tag, ImageAttributes.originalImage)
}

object Backend {
  class InvalidImageError extends RuntimeException
}