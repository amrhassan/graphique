package graphique.backends

class RequestedImageCache(io: IO, paths: Paths) {

  /**
   * Checks whether this requested image is cached.
   *
   * @throws IOError when something evil happens
   */
  def has(requestedImage: RequestedImage): Boolean = !availableImages(requestedImage).isEmpty

  /**
   * Returns the set of image IDs available for the requested images.
   */
  def availableImages(requestedImage: RequestedImage): Set[ImageId] = {
    val possiblePaths = (paths possibleImageIds requestedImage) map { id => paths ofImage id }
    val availablePaths = possiblePaths filter io.exists
    availablePaths map { path =>
      path.getFileName.toString
    }
  }

  /**
   * Caches the resulting image for this request for later.
   *
   * @throws IOError
   */
  def store(id: ImageId, image: Array[Byte]): Unit =
    (io write (paths ofImage id))(image)

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
