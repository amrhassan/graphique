package graphique.localbackend

import graphique.ImageAttributes

/**
 * The HTTP image server
 */
class ImageServer(val port: Int) {

  def urlFor(tag: String): Option[String] = ???

  def urlFor(tag: String, attributes: ImageAttributes): Option[String] = ???
}
