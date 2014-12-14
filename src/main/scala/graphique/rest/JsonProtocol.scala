package graphique.rest

import graphique.service.GraphiqueService.RequestedImageUrl
import spray.json.DefaultJsonProtocol

object JsonProtocol extends DefaultJsonProtocol {
  implicit val RequestedImageUrlFormat = jsonFormat1(RequestedImageUrl)
}