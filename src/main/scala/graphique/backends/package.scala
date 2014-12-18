package graphique


package object backends {

  type IOError = java.io.IOError

  class InvalidImageError extends RuntimeException

  case class ImageProcessingError(image: Image, cause: Throwable)
    extends RuntimeException(s"Failed while processing the image for $image", cause)

  case class SourceImageNotFoundError(tag: ImageTag) extends RuntimeException

  type ImageContent = Array[Byte]
  type ImageId = String
  type ImageTag = String

  type UrlProvider = (ImageId => String)
}
