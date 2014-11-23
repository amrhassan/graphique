package graphique

import graphique.image.ImageAttributes

/**
 * An image storing/serving backend that handles all the necessary low-level work.
 */
trait Backend {

  /**
   * Submits an image to the backend.
   *
   * The submitted image may overwrite a previously submitted image if they share the same tag.
   *
   * @param image the submitted image
   * @param tag the identifier of the submitted image
   * @throws ImageSubmissionError when the image submission fails
   */
  def submit(image: Array[Byte], tag: String)

  /**
   * Looks up and returns an HTTP URL for the image identified by the given tag and in the specified
   * dimensions.
   *
   * @param tag the requested image tag identifier
   * @param attributes the set of desired attributes of the returned image
   * @return an image URL if it were available existed
   */
  def imageUrl(tag: String, attributes: ImageAttributes): Option[String]
}
