package graphique.backends

import graphique.images

case class ImageProcessingError(imageTag: String, desiredAttributes: images.ImageAttributes, cause: Throwable)
  extends RuntimeException(s"Failed while processing the image for $imageTag: $desiredAttributes", cause)
