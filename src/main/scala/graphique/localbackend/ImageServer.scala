package graphique.localbackend

import graphique.Dimensions

/**
 * The HTTP image server
 */
class ImageServer(val port: Int) {

  def urlFor(tag: String): Option[String] = ???

  def urlFor(tag: String, dimensions: Dimensions): Option[String] = ???
}
