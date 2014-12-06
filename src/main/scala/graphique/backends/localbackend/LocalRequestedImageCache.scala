package graphique.backends.localbackend

import graphique.backends.abstractbackend.{RequestedImage, RequestedImageCache}

class LocalRequestedImageCache(filePaths: FilePaths, io: LocalIO) extends RequestedImageCache{

  override def has(requestedImage: RequestedImage): Boolean =
    io exists (filePaths ofImage requestedImage)

  override def store(requestedImage: RequestedImage, image: Array[Byte]): Unit =
    io writeData(image, filePaths ofImage requestedImage)

  override def clearTaggedWith(tag: String): Unit = {
    val (directory, filenameGlob) = filePaths imagePathScheme tag
    io deleteFiles(directory, filenameGlob)
  }
}
