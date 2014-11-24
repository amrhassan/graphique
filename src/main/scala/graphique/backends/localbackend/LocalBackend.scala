package graphique.backends.localbackend

import graphique.backends.Backend
import graphique.image

private[localbackend] trait LocalBackend extends Backend {

  def io: IO

  def filePaths: FilePaths

  def imageServer: ImageServer

  def submit(rawImage: Array[Byte], tag: String) {
    io.writeData(rawImage, filePaths.ofRawImage(tag))
  }

  def imageUrl(tag: String, attributes: image.Attributes): Option[String] =
    imageServer.urlFor(tag, attributes)
}
