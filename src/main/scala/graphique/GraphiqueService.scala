package graphique

import akka.actor.{Actor, ActorLogging}
import graphique.backends._

import scala.concurrent.Future

/**
 * The entry point to the Graphique microservice.
 */
class GraphiqueService(backend: Backend) extends Actor with ActorLogging {

  import GraphiqueService._
  import scala.concurrent.ExecutionContext.Implicits.global

  override final def receive: Receive = {

    case SubmitImage(image) =>

      log info s"Submitting an ${image.length}B image with the tag"
      val originalSender = sender
      Future {
        try {
          val tag = backend submitImage image
          originalSender ! ImageSubmissionOK(tag)
        } catch {
          case e: InvalidImageError => originalSender ! InvalidSubmittedImage
        }
      }

    case RequestImage(tag, attributes, makeSureExists) =>

      log info s"Requesting the image $tag (makeSureExists=$makeSureExists) with attributes $attributes"
      val originalSender = sender
      Future {
        try {

          val url = if (makeSureExists) {
            backend urlForExistingImage(tag, attributes)
          } else {
            backend imageUrlFor(tag, attributes)
          }

          originalSender ! Image(url)

        } catch {
          case SourceImageNotFoundError(_) => originalSender ! SourceImageNotFound(tag)
        }
      }
  }
}

object GraphiqueService {

  /**
   * Submit an image.
   *
   * This request message is responded to by either an ImageSubmissionOK or an InvalidSubmittedImage
   * message.
   *
   * @param image the binary content of the image
   */
  case class SubmitImage(image: ImageContent)

  case class ImageSubmissionOK(tag: ImageTag)

  case object InvalidSubmittedImage

  /**
   * Request an HTTP URL for the image identified by the given tag and in the specified attributes.
   *
   * This request message is responded to by either `Image` or `SourceImageNotFound`.
   *
   * @param tag the identifier tag of the requested image
   * @param attributes the attributes of the requested image
   * @param makeSurExists instructs the service to make sure the image exists
   */
  case class RequestImage(tag: String, attributes: images.ImageAttributes, makeSurExists: Boolean)

  case class SourceImageNotFound(tag: ImageTag)

  case class Image(url: String)
}

