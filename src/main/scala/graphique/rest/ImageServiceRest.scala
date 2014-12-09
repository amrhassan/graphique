package graphique.rest

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import graphique.images._
import spray.http.StatusCodes
import spray.httpx.unmarshalling._
import spray.routing.Route

import scala.concurrent.ExecutionContext
import scala.util.Try

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

  val extractImageAttributes = parameters(
    'sizeWithin.?.as[Option[Dimensions]](dimensionsOptionDeserializer),
    'format.?.as(imageFormatOptionDeserializer)
  )

  override lazy val route: Route =
    put {
      path("submit") {
        parameters('tag) { tag =>
          requestEntityPresent {
            entity(as[Array[Byte]]) { image =>
              onSuccess (imageService ? SubmitImage(image, tag)) {  // This detaches to an asynchronous call
                case ImageSubmissionOK => complete(StatusCodes.OK)
                case InvalidSubmittedImage => complete(StatusCodes.BadRequest)
              }
            }
          }
        }
      }
    } ~
    get {
      path("image" / """[^/]+""".r ~ Slash.?) { tag =>

        extractImageAttributes { (dimensions, format) =>

          var imageAttributes = ImageAttributes.originalImage
          for (requestedSize <- dimensions)
            imageAttributes = imageAttributes.resizedTo(requestedSize)
          for (requestedFormat <- format)
            imageAttributes = imageAttributes.transcodedTo(requestedFormat)

          onSuccess(imageService ? RequestImageUrl(tag, imageAttributes)) {
            case RequestedImageNotFound => complete(StatusCodes.NotFound, "Requested image not found")
            case RequestedImageUrl(url) => complete(StatusCodes.OK, url)
          }
        }
      }
    }
}