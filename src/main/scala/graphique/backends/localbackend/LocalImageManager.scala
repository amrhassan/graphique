package graphique.backends.localbackend

import graphique.backends.abstractbackend.{RequestedImage, ImageManager}

class LocalImageManager(filePaths: FilePaths, io: IO) extends ImageManager{

  override def isImageCached(requestedImage: RequestedImage): Boolean =
    io exists (filePaths ofImage requestedImage)

  override def cacheImage(requestedImage: RequestedImage, image: Array[Byte]): Unit =
    io writeData(image, filePaths ofImage requestedImage)

  override def clearImageCaches(tag: String): Unit = {
    val (directory, filenameGlob) = filePaths imagePathScheme tag
    io deleteFiles(directory, filenameGlob)
  }
}
