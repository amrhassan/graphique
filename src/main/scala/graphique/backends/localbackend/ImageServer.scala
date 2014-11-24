package graphique.backends.localbackend

import graphique.image

/**
 * The HTTP image server
 */
class ImageServer(val port: Int) {

  def urlFor(tag: String): Option[String] = ???

  def urlFor(tag: String, attributes: image.ImageAttributes): Option[String] = ???
}
