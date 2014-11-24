package graphique.backends.abstractbackend

import graphique.backends.{ImageSubmissionError, Backend}
import graphique.image.Attributes
import graphique.image

trait AbstractBackend extends Backend {

  /**
   * Stores the raw image.
   *
   * @param tag the identifier tag
   * @param image the raw image
   * @throws IOError
   */
  protected def storeRaw(tag: String, image: Array[Byte]): Unit

  /**
   * Reads and returns the content of the raw image if it exists.
   *
   * @throws IOError
   */
  protected def readRaw(tag: String): Option[Array[Byte]]

  /**
   * Checks whether this requested image is cached.
   *
   * @throws IOError
   */
  protected def isCached(requestedImage: RequestedImage): Boolean

  /**
   * Caches this requested image for later.
   *
   * @throws IOError
   */
  protected def storeCached(requestedImage: RequestedImage, image: Array[Byte]): Unit

  /**
   * Clears any caches for the image identified by the given tag.
   *
   * @param tag the image identifier
   * @throws IOError
   */
  protected def clearCaches(tag: String): Unit

  /**
   * Returns the URL used to serve this cached image.
   *
   * @throws IOError
   */
  protected def cachedImageURL(requestedImage: RequestedImage): Option[String]

  override final def submit(image: Array[Byte], tag: String): Unit = {

    // TODO: Log this call

    try {
      clearCaches(tag)
      storeRaw(tag, image)
    } catch {
      case IOError(message, cause) =>
        throw ImageSubmissionError("Failed to submit " + tag, cause)
    }
  }

  override final def imageUrl(tag: String, attributes: Attributes): Option[String] = {

    // TODO: Log this call

    val requestedImage = RequestedImage(tag, attributes)
    lazy val imageProcessor = new image.Processor(attributes)

    def cacheImageIfNotCached: Unit = {

      if (isCached(requestedImage))
        return

      readRaw(tag) flatMap { rawImage =>
        ???
        // TODO: Process the raw image and cache it
      }
    }

    cachedImageURL(requestedImage)
  }
}
