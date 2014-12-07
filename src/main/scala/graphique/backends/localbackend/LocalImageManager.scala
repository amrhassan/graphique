package graphique.backends.localbackend

import graphique.backends.{URLProvider, RequestedImageCache, ImageManager}

class LocalImageManager(val cache: RequestedImageCache,
                        val urlScheme: URLProvider) extends ImageManager
