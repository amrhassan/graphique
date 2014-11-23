package graphique

import graphique.attributes.{Format, OriginalFormat, OriginalSize, Size}

case class ImageAttributes(size: Size = OriginalSize, format: Format = OriginalFormat) {

  /**
   * Returns a copy of these image attributes with the given size.
   */
  def ofSize(size: Size): ImageAttributes = copy(size, format)

  /**
   * Returns a copy of these image attributes with the given format.
   */
  def ofFormat(format: Format): ImageAttributes = copy(size, format)
}
