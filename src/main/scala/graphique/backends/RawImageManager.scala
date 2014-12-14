package graphique.backends

class RawImageManager(paths: Paths, io: IO) {

  /**
   * Stores the raw image. Overwriting the earlier stored raw image if present.
   *
   * @param tag the identifier tag
   * @param image the raw image
   * @throws IOError when something evil happens
   */
  def store(tag: String, image: Array[Byte]): Unit = {
    val path = paths ofRawImage tag
    (io write path)(image)
  }

  /**
   * Reads and returns the content of the raw image if it exists.
   *
   * @throws IOError when something evil happens
   */
  def read(tag: String): Option[Array[Byte]] = {
    val path = paths ofRawImage tag

    if (io exists path)
      Some(io read path)
    else
      None
  }
}
