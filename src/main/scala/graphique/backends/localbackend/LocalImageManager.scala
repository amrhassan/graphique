package graphique.backends.localbackend

import graphique.backends.{UrlProvider, RequestedImageCache, ImageManager}

class LocalImageManager(val cache: RequestedImageCache,
                        val urlScheme: UrlProvider) extends ImageManager
