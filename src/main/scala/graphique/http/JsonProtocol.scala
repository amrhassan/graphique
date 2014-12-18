package graphique.http

import graphique.GraphiqueService
import GraphiqueService.Image
import spray.json.DefaultJsonProtocol

object JsonProtocol extends DefaultJsonProtocol {
  implicit val ImageFormat = jsonFormat1(Image)
}