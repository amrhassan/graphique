package graphique.backends.abstractbackend

import com.typesafe.scalalogging.LazyLogging
import graphique.backends.{ImageProcessingError, ImageSubmissionError, Backend}
import graphique.image

/**
 * An abstract implementation of a Backend. Subclass and provide concrete implementations for
 * the abstract members and you're good to go!
 */
trait AbstractBackend extends Backend with LazyLogging {

  /**
   * The manager of raw images.
   */
  protected def rawImages: RawImageManager

  /**
   * The manager of processed and servable images.
   */
  protected def images: ImageManager

  override final def submit(image: Array[Byte], tag: String): Unit = {

    logger info s"Submitting an ${image.length}B image with the tag: $tag"

    try {

      // This mutates the shared state of the image server (the stored files)
      // and is probably a bad idea.
      images.cache clearTaggedWith tag

      rawImages store(tag, image)

    } catch {
      case e: IOError => throw new ImageSubmissionError("Failed to submit " + tag, e)
    }
  }

  override final def imageUrl(tag: String, attributes: image.ImageAttributes): Option[String] = {
    logger info s"Requesting the image ${tag} with attributes $attributes"
    images imageUrl(RequestedImage(tag, attributes), rawImages read tag)
  }
}
