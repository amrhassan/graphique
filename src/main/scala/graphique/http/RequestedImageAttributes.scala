package graphique.http

import graphique.images.{ImageAttribute, ImageAttributes, ImageFormat, Dimensions}

case class RequestedImageAttributes(size: Option[Dimensions], format: Option[ImageFormat]) {

  lazy val attributes = {
    ImageAttributes(
      (size map ImageAttribute.SizeWithin) getOrElse ImageAttribute.OriginalSize,
      (format map ImageAttribute.TranscodedFormat) getOrElse ImageAttribute.OriginalFormat
    )
  }
}
