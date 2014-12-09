package graphique.backends

trait UrlProvider {

  /**
   * Returns the servable URL for the requested image, granted it has been processed and
   * cached by an ImageManager.
   */
  def forRequestedImage(requestedImage: RequestedImage): Option[String]
}
