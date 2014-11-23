package graphique.localbackend

import graphique.image.Attributes$

/**
 * The HTTP image server
 */
class ImageServer(val port: Int) {

  def urlFor(tag: String): Option[String] = ???

  def urlFor(tag: String, attributes: Attributes): Option[String] = ???
}
