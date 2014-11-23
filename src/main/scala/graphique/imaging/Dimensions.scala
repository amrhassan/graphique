package graphique.imaging

/**
 * Dimensions of an image.
 *
 * @param width
 * @param height
 */
case class Dimensions(width: Int, height: Int) {

  /**
   * The canonical representation of these dimensions as a String.
   */
  lazy val canonicalString: String = "%dx%d" format(width, height)
}
