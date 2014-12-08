package graphique.rest

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import spray.http.StatusCodes
import spray.routing.Route

import scala.concurrent.ExecutionContext

/**
 * The REST inteface to the ImageService actor.
 */
class ImageServiceRest(imageService: ActorRef, implicit val imageServiceTimeout: Timeout) extends HttpServiceListener {

  import graphique.service.ImageService._

  implicit val executionContext: ExecutionContext = context.system.dispatcher

  override lazy val route: Route =
    put {
      path("submit") {
        parameters('tag) { tag =>
          requestEntityPresent {
            entity(as[Array[Byte]]) { image =>
              onSuccess (imageService ? SubmitImage(image, tag)) {
                case ImageSubmissionOK => complete(StatusCodes.OK)
                case InvalidSubmittedImage => complete(StatusCodes.BadRequest)
              }
            }
          }
        }
      }
    } ~
    get {
      path("image-url") {
        parameters('tag.as[String]) {
          ???
        }
      }
    }
}