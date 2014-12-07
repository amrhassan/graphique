package graphique.backends.localbackend

import graphique.backends.{URLProvider, RequestedImageCache, ImageManager}
import graphique.images.ImageProcessor

class LocalImageManager(val cache: RequestedImageCache,
                        val urlScheme: URLProvider,
                        val imageProcessor: ImageProcessor) extends ImageManager
