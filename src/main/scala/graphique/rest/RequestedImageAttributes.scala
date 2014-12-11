package graphique.rest

import graphique.images.{ImageAttributes, ImageFormat, Dimensions}

case class RequestedImageAttributes(size: Option[Dimensions], format: Option[ImageFormat]) {

  lazy val toImageAttributes = {
    var imageAttributes = ImageAttributes.originalImage
    for (requestedSize <- size)
      imageAttributes = imageAttributes.resizedTo(requestedSize)
    for (requestedFormat <- format)
      imageAttributes = imageAttributes.transcodedTo(requestedFormat)
    imageAttributes
  }
}
