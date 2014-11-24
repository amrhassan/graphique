package graphique.backends.localbackend

import graphique.backends.abstractbackend.{RequestedImage, URLProvider}

/**
 * The HTTP image server
 */
class ImageServer(val port: Int, filePaths: FilePaths) extends URLProvider {

  override def forRequestedImage(requestedImage: RequestedImage): Option[String] = ???
}
