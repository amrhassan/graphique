package graphique.backends

class RequestedImageCache(io: IO, paths: Paths) {

  /**
   * Checks whether this requested image is cached.
   *
   * @throws IOError when something evil happens
   */
  def has(requestedImage: RequestedImage): Boolean =
    io exists (paths ofImage requestedImage)

  /**
   * Caches the resulting image for this request for later.
   *
   * @throws IOError
   */
  def store(requestedImage: RequestedImage, image: Array[Byte]): Unit =
    (io write (paths ofImage requestedImage))(image)

  /**
   * Clears any caches for the image identified by the given tag.
   *
   * @param tag the image identifier
   * @throws IOError
   */
  def clearTaggedWith(tag: String): Unit = {
    val (directory, prefix) = paths imagePathScheme tag
    io delete(directory, prefix)
  }
}
