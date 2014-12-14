package graphique.service

import akka.actor.{Actor, ActorLogging}
import graphique.backends.Backend
import graphique.images

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * The entry point to the Graphique microservice.
 */
class GraphiqueService(backend: Backend) extends Actor with ActorLogging {

  import graphique.service.GraphiqueService._

  override final def receive: Receive = {

    case SubmitImage(image, tag) =>
      val originalSender = sender

      Future {
        log info s"Submitting an ${image.length}B image with the tag: $tag"

        try {
          backend submitImage(tag, image)
          originalSender ! ImageSubmissionOK
        } catch {
          case e: Backend.InvalidImageError => originalSender ! InvalidSubmittedImage
        }
      }

    case RequestImageUrl(tag, attributes) =>
      val originalSender = sender

      Future {
        log info s"Requesting the image $tag with attributes $attributes"
        val urlOption = backend imageUrlFor(tag, attributes)
        urlOption match {
          case Some(url) => originalSender ! RequestedImageUrl(url)
          case None => originalSender ! RequestedImageNotFound
        }
      }
  }
}

object GraphiqueService {

  /**
   * Submit an image.
   *
   * The submitted image may overwrite a previously submitted image if they share the same tag.
   *
   * This request message is responded to always by either an ImageSubmissionOK or an InvalidSubmittedImage
   * message.
   *
   * @param image the binary content of the image
   * @param tag the tag identifier associated with the image
   */
  case class SubmitImage(image: Array[Byte], tag: String)

  case object ImageSubmissionOK

  case object InvalidSubmittedImage

  /**
   * Request an HTTP URL for the image identified by the given tag and in the specified attributes.
   *
   * This request message is responded to always by one of the following response messages:
   *  * RequestedImageNotFound
   *  * RequestedImageUrl(url)
   *
   * @param tag the identifier tag of the requested image
   * @param attributes the attributes of the requested image
   */
  case class RequestImageUrl(tag: String, attributes: images.ImageAttributes)

  case object RequestedImageNotFound

  case class RequestedImageUrl(url: String)
}

