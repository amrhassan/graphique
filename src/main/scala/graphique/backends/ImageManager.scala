package graphique.backends

import graphique.images.ImageProcessor

import scala.util.Try

class ImageManager(io: IO, paths: Paths) {

  private val imageProcessor = new ImageProcessor

  /**
   * Submits a new image.
   *
   * @param imageContent the image content
   * @throws InvalidImageError when the submitted content is not a valid image
   * @throws IOError when evil IO stuff happens
   * @throws InvalidImageError
   * @return the tag of the submitted image
   */
  def submit(imageContent: ImageContent): ImageTag = {
    val image = Image.from(imageContent)
    store(imageContent, image)
    image.tag
  }

  /**
   * Creates the requested image in the system.
   *
   * @throws SourceImageNotFoundError when the source of the requested image is not available
   * @throws ImageProcessingError
   * @throws IOError
   */
  def createImage(image: Image): Unit = {
    val source: ImageContent =
      Try(io read (paths ofImage image.sourceId)) getOrElse (throw SourceImageNotFoundError(image.tag))
    val errorOrImage = imageProcessor.process(source, image.attributes)
    errorOrImage match {
      case Right(processedImage) => store(processedImage, image)
      case Left(error) => throw ImageProcessingError(image, error)
    }
  }

  /**
   * Checks whether the image is available.
   */
  def has(image: Image): Boolean = {
    io exists (paths ofImage image.id)
  }

  /**
   * Stores an image content.
   */
  private def store(imageContent: ImageContent, image: Image): Unit = {
    val path = paths ofImage image.id
    (io write path)(imageContent)
  }
}

