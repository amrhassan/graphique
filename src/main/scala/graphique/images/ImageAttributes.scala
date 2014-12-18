package graphique.images

/**
 * The attributes of an image.
 * 
 * Usage example:
 *  
 *  To specify that an image should carry the attributes of the original image
 *  but resized to fit within a 120 by 100 rectangle:
 *  
 *  val attributes = ImageAttributes.originalImage(JPEGFormat).resizedTo(Dimensions(120, 100))
 *  
 *  To specify a PNG version of that image:
 *  
 *  val pngVersion = attributes.transcodedTo(PNGFormat)
 */
case class ImageAttributes(size: ImageAttribute.Size, format: ImageAttribute.Format) {

  /**
   * Specifies that the image should fit within the specified size.
   *
   * This attribute usually results in a resize with respect to the aspect ratio
   * of the original image.
   */
  def resizedTo(dimensions: Dimensions): ImageAttributes =
    copy(size = ImageAttribute.SizeWithin(dimensions))

  /**
   * Specifies that the image should be in the given format.
   */
  def transcodedTo(format: ImageFormat): ImageAttributes =
    copy(format = ImageAttribute.TranscodedFormat(format))

  def isUnmodified: Boolean =
    (size == ImageAttribute.OriginalSize) && (format == ImageAttribute.OriginalFormat)
}

case object ImageAttributes {
  lazy val originalImage =
    ImageAttributes(size = ImageAttribute.OriginalSize, format = ImageAttribute.OriginalFormat)
}
