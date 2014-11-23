package graphique.image

/**
 * The attributes of an image.
 * 
 * Usage example:
 *  
 *  To specify that an image should carry the attributes of the original image
 *  but resized to fit within a 120 by 100 rectangle:
 *  
 *  val attributes = ImageAttributes.originalImage.resizedTo(Dimensions(120, 100))
 *  
 *  To specify a PNG version of that image:
 *  
 *  val pngVersion = attributes.transcodedTo(PNGFormat)
 */
case class Attributes private(size: Attribute.Size, format: Attribute.Format) {

  /**
   * Specifies that the image should fit within the specified size.
   *
   * This attribute usually results in a resize with respect to the aspect ratio
   * of the original image.
   */
  def resizedTo(dimensions: Dimensions): Attributes =
    copy(size = Attribute.SizeWithin(dimensions))

  /**
   * Specifies that the image should be in the given format.
   */
  def transcodedTo(format: Format): Attributes =
    copy(format = Attribute.TranscodedFormat(format))
}

case object Attributes {

  /**
   * The attributes of the original image unchanged.
   */
  lazy val originalImage =
    Attributes(size = Attribute.OriginalSize, format = Attribute.OriginalFormat)
}
