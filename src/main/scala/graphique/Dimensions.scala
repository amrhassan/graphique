package graphique

case class Dimensions(width: Int, height: Int) {
  lazy val canonicalString: String = "%dx%d" format(width, height)
}
