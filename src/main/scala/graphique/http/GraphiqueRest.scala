package graphique.http

import java.util.concurrent.{Executors, Executor, TimeUnit}

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import graphique.GraphiqueService
import graphique.images._
import spray.http._
import spray.routing.Route

import scala.concurrent.ExecutionContext

/**
 * The REST interface to the ImageService actor.
 */
class GraphiqueRest(graphiqueService: ActorRef, threadPoolSize: Int) extends HttpServiceListener {

  import GraphiqueService._

  import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
  import JsonProtocol._
  import ImageAttributeDeserializers._
  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(threadPoolSize))

  implicit val timeout: Timeout = Timeout(1, TimeUnit.DAYS)

  val extractImageAttributes = parameters(
    "size-within".?.as[Option[Dimensions]](dimensionsOptionDeserializer),
    'format.?.as[Option[ImageFormat]](imageFormatOptionDeserializer)
  ).as(RequestedImageAttributes)

  override lazy val route: Route = {

    path("images" ~ Slash.?) {
      (post & requestEntityPresent & entity(as[Array[Byte]])) { imageContent =>
        detach() {
          onSuccess(graphiqueService ? SubmitImage(imageContent)) {
            case ImageSubmissionOK(tag) =>
              val headers = List(HttpHeaders.Location(Uri(s"/image/$tag")))
              complete(HttpResponse(StatusCodes.Created, headers = headers))
            case InvalidSubmittedImage =>
              complete(HttpResponse(StatusCodes.BadRequest))
            case UnexpectedFailure(_) =>
              complete(HttpResponse(StatusCodes.InternalServerError))
          }
        }
      }
    } ~
      (path("image" / """.*""".r) & extractImageAttributes) { (tag, requestedImageAttributes) =>
        get {
          detach() {
            onSuccess(graphiqueService ? RequestImage(tag, requestedImageAttributes.attributes, makeSurExists = false)) {
              case image: Image => complete(image)
              case SourceImageNotFound(_) => complete(StatusCodes.NotFound)
              case UnexpectedFailure(_) =>
                complete(HttpResponse(StatusCodes.InternalServerError))
            }
          }
        } ~
        patch {
          detach() {
            onSuccess(graphiqueService ? RequestImage(tag, requestedImageAttributes.attributes, makeSurExists = true)) {
              case image: Image => complete(image)
              case SourceImageNotFound(_) => complete(StatusCodes.NotFound)
              case UnexpectedFailure(_) =>
                complete(HttpResponse(StatusCodes.InternalServerError))
            }
          }
        }
     }
  }
}