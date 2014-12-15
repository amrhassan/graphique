package graphique.backends.localbackend.imageserver

import java.nio.file.Path

import graphique.http.HttpServiceListener
import spray.routing.Route
import spray.routing.directives.ContentTypeResolver

class ImageServer(imagePath: Path) extends HttpServiceListener {
  
  implicit val resolver: ContentTypeResolver = new MimeMagicOrExtensionContentTypeResolver(imagePath)
  
  def route: Route = {
    getFromDirectory(imagePath.toString)
  }
}