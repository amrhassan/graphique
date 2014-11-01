package graphique

/**
 * An image storing/serving backend that handles all the necessary low-level work.
 */
trait Backend {

  /**
   * Submits a raw image to the backend.
   *
   * The submitted raw image may overwrite a previously submitted raw image if they share the same
   * tag.
   *
   * @param rawImage the submitted image
   * @param tag the identifier of the submitted image
   */
  def submit(rawImage: Array[Byte], tag: String)

  /**
   * Looks up and returns an HTTP URL for the image identified by the given tag and in the specified
   * dimensions.
   *
   * @param tag the requested image tag identifier
   * @param width the requested image width
   * @param height the requested image height
   * @return an image URL if it were available existed
   */
  def imageUrl(tag: String, width: Int, height: Int): Option[String]
}
