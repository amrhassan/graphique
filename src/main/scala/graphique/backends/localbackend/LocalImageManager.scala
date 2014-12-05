package graphique.backends.localbackend

import graphique.backends.abstractbackend.{RequestedImageCache, URLProvider, ImageManager}
import graphique.image.ImageProcessor

class LocalImageManager(val cache: RequestedImageCache, val urlScheme: URLProvider,
                         val imageProcessor: ImageProcessor) extends ImageManager
