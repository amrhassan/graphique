package graphique.http

import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import graphique.GraphiqueService
import graphique.images._
import spray.http.StatusCodes
import spray.routing.Route
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{FiniteDuration, Duration}

/**
 * The REST interface to the ImageService actor.
 */
class GraphiqueRest(graphiqueService: ActorRef) extends HttpServiceListener {

  import GraphiqueService._

  import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
  import JsonProtocol._
  import ImageAttributeDeserializers._

  implicit val timeout: Timeout = Timeout(1, TimeUnit.DAYS)

  val extractImageAttributes = parameters('size.?.as[Option[Dimensions]](dimensionsOptionDeserializer),
    'format.?.as(imageFormatOptionDeserializer)).as(RequestedImageAttributes)

  override lazy val route: Route = {
    path("image" / """[^/]+""".r ~ Slash.?) { tag =>
      (put & requestEntityPresent & entity(as[Array[Byte]])) { image =>
        detach() {
          onSuccess(graphiqueService ? SubmitImage(image, tag)) {
            // This detaches to an asynchronous call
            case ImageSubmissionOK => complete(StatusCodes.Created)
            case InvalidSubmittedImage => complete(StatusCodes.BadRequest)
          }
        }
      } ~
      (get & extractImageAttributes) { requestedImageAttributes =>
        detach() {
          onSuccess(graphiqueService ? RequestImageUrl(tag, requestedImageAttributes.toImageAttributes)) {
            case RequestedImageNotFound => complete(StatusCodes.NotFound, "Requested image not found")
            case message: RequestedImageUrl => complete(message)
          }
        }
      }
    }
  }
}