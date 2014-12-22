package graphique

import java.util.concurrent.Executors

import akka.actor.{Actor, ActorLogging}
import graphique.backends._

import scala.concurrent.{ExecutionContext, Future}

/**
 * The entry point to the Graphique microservice.
 */
class GraphiqueService(backend: Backend, threadPoolSize: Int) extends Actor with ActorLogging {

  import GraphiqueService._
  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(threadPoolSize))

  override final def receive: Receive = {

    case SubmitImage(image) =>

      log info s"Submitting a ${image.length}B image"
      val originalSender = sender
      Future {
        try {
          val tag = backend submitImage image
          log info s"Submitted successfully with tag $tag"
          originalSender ! ImageSubmissionOK(tag)
        } catch {
          case e: InvalidImageError => originalSender ! InvalidSubmittedImage
        }
      } onFailure {
        case e: Throwable  => log error(e, "Unexpected error")
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
      } onFailure {
        case e: Throwable  => log error(e, "Unexpected error")
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

