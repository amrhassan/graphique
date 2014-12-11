package graphique.rest

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import graphique.images._
import spray.http.StatusCodes
import spray.httpx.unmarshalling._
import spray.routing.Route

import scala.concurrent.ExecutionContext

/**
 * The REST inteface to the ImageService actor.
 */
class ImageServiceRest(imageService: ActorRef, implicit val imageServiceTimeout: Timeout) extends HttpServiceListener {

  import graphique.service.ImageService._

  implicit val executionContext: ExecutionContext = context.system.dispatcher

  val dimensionsOptionDeserializer = new FromStringOptionDeserializer[Option[Dimensions]] {
    val Pattern = """(\d+)[xX](\d+)""".r
    def apply(optionString: Option[String]) = optionString map {
      case Pattern(width, height) => Right(Some(Dimensions(width.toInt, height.toInt)))
      case _ => Left(new MalformedContent("Dimensions must be formatted like WIDTHxHeight"))
    } getOrElse Right(None)
  }

  val imageFormatOptionDeserializer = new FromStringOptionDeserializer[Option[ImageFormat]] {
    val JPEGPattern = """(?i)JPEG\(([0-9.]+)\)""".r
    def apply(optionString: Option[String]) = optionString map {
      case "PNG" => Right(Some(PNGFormat))
      case "JPEG" => Right(Some(JPEGFormat()))
      case JPEGPattern(quality) if quality.toFloat <= 1 => Right(Some(JPEGFormat(quality.toFloat)))
      case _ => Left(
        new MalformedContent("The image format must be one of [JPEG, JPEG(quality), PNG] where (0 <= quality <= 1)"))
    } getOrElse Right(None)
  }

  case class RequestedImageAttributes(size: Option[Dimensions], format: Option[ImageFormat]) {
    lazy val toImageAttributes = {
      var imageAttributes = ImageAttributes.originalImage
      for (requestedSize <- size)
        imageAttributes = imageAttributes.resizedTo(requestedSize)
      for (requestedFormat <- format)
        imageAttributes = imageAttributes.transcodedTo(requestedFormat)
      imageAttributes
    }
  }

  val extractImageAttributes = parameters('size.?.as[Option[Dimensions]](dimensionsOptionDeserializer),
    'format.?.as(imageFormatOptionDeserializer)).as(RequestedImageAttributes)

  override lazy val route: Route = {
    path("image" / """[^/]+""".r ~ Slash.?) { tag =>
      (put & requestEntityPresent & entity(as[Array[Byte]])) { image =>
        onSuccess (imageService ? SubmitImage(image, tag)) {  // This detaches to an asynchronous call
          case ImageSubmissionOK => complete(StatusCodes.Created)
          case InvalidSubmittedImage => complete(StatusCodes.BadRequest)
        }
      } ~
      (get & extractImageAttributes) { requestedImageAttributes =>
        onSuccess(imageService ? RequestImageUrl(tag, requestedImageAttributes.toImageAttributes)) {
          case RequestedImageNotFound => complete(StatusCodes.NotFound, "Requested image not found")
          case RequestedImageUrl(url) => complete(StatusCodes.OK, url)
        }
      }
    }
  }
}