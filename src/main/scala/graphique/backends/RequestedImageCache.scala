package graphique.backends

trait RequestedImageCache {

  /**
   * Checks whether this requested image is cached.
   *
   * @throws IOError when something evil happens
   */
  def has(requestedImage: RequestedImage): Boolean

  /**
   * Caches the resulting image for this request for later.
   *
   * @throws IOError
   */
  def store(requestedImage: RequestedImage, image: Array[Byte]): Unit

  /**
   * Clears any caches for the image identified by the given tag.
   *
   * @param tag the image identifier
   * @throws IOError
   */
  def clearTaggedWith(tag: String): Unit
}
