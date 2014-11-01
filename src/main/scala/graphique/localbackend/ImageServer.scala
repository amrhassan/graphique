package graphique.localbackend

/**
 * The HTTP image server
 */
class ImageServer(val port: Int) {

  def urlFor(tag: String): Option[String] = ???

  def urlFor(tag: String, width: Int, height: Int): Option[String] = ???
}
