package graphique

case class Dimensions(width: Int, height: Int) extends Tuple2(width, height) {
  lazy val canonicalString: String = "%dx%d" format(width, height)
}
