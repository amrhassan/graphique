package graphique.backends.abstractbackend

trait ImageManager {

  /**
   * Checks whether this requested image is cached.
   *
   * @throws IOError when something evil happens
   */
  def isImageCached(requestedImage: RequestedImage): Boolean

  /**
   * Caches this requested image for later.
   *
   * @throws IOError
   */
  def cacheImage(requestedImage: RequestedImage, image: Array[Byte]): Unit

  /**
   * Clears any caches for the image identified by the given tag.
   *
   * @param tag the image identifier
   * @throws IOError
   */
  def clearImageCaches(tag: String): Unit
}
