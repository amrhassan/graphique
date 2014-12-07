package graphique.backends

import graphique.images

/**
 * The definition of a requested image.
 *
 * @param tag the identifier tag of the requested image
 * @param attributes the attributes of the requested image
 */
case class RequestedImage(tag: String, attributes: images.ImageAttributes)
