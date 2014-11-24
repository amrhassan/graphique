package graphique.backends

import graphique.image

case class ImageProcessingError(imageTag: String, desiredAttributes: image.ImageAttributes, cause: Throwable)
  extends RuntimeException(s"Failed while processing the image for $imageTag: $desiredAttributes", cause)
