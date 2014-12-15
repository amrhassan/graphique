package graphique.backends

trait UrlProvider {
  def forImage(id: ImageId): String
}
