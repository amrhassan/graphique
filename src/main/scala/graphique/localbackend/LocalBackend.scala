package graphique.localbackend

import graphique.{ImageAttributes, Backend}

private[localbackend] trait LocalBackend extends Backend {

  def io: IO

  def filePaths: FilePaths

  def imageServer: ImageServer

  def submit(rawImage: Array[Byte], tag: String) {
    io.writeData(rawImage, filePaths.ofRawImage(tag))
  }

  def imageUrl(tag: String, attributes: ImageAttributes): Option[String] =
    imageServer.urlFor(tag, attributes)
}
