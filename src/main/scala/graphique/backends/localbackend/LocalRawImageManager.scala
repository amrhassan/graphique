package graphique.backends.localbackend

import graphique.backends.RawImageManager

class LocalRawImageManager(filePaths: FilePaths, io: LocalIO) extends RawImageManager{

  override def store(tag: String, image: Array[Byte]): Unit = {
    val path = filePaths ofRawImage tag
    io writeData(image, path)
  }

  override def read(tag: String): Option[Array[Byte]] = {
    val path = filePaths ofRawImage tag

    if (io exists path)
      Some(io readData path)
    else
      None
  }
}
