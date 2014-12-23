package graphique

import java.util.concurrent.Executors

import akka.actor.{Actor, ActorLogging}
import com.typesafe.scalalogging.Logger
import graphique.backends._
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}

/**
 * The entry point to the Graphique microservice.
 */
class GraphiqueService(backend: Backend, threadPoolSize: Int) extends Actor {

  import GraphiqueService._
  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(threadPoolSize))

  val log = Logger(LoggerFactory.getLogger("GraphiqueService"))

  override final def receive: Receive = {

    case SubmitImage(image) =>

      val originalSender = sender
      Future {
        log info s"Submitting a ${image.length} byte image"
        try {
          val tag = backend submitImage image
          log info s"Submitted $tag successfully"
          originalSender ! ImageSubmissionOK(tag)
        } catch {
          case e: InvalidImageError => originalSender ! InvalidSubmittedImage
        }
      } onFailure {
        case e: Throwable  =>
          log error("Unexpected error", e)
          originalSender ! UnexpectedFailure(e)
      }

    case RequestImage(tag, attributes, makeSureExists) =>

      val originalSender = sender
      Future {

        if (makeSureExists)
          log info s"Requesting $tag with attributes $attributes"
        else
          log info s"Requesting $tag (makeSureExists=$makeSureExists) with attributes $attributes"

        try {

          val url = if (makeSureExists) {
            backend urlForExistingImage(tag, attributes)
          } else {
            backend imageUrlFor(tag, attributes)
          }

          originalSender ! Image(url)
          log info s"Image request fulfilled successfully"

        } catch {
          case SourceImageNotFoundError(_) => originalSender ! SourceImageNotFound(tag)
        }
      } onFailure {
        case e: Throwable  => 
          log error("Unexpected error", e)
          originalSender ! UnexpectedFailure(e)
      }
  }
}

object GraphiqueService {

  /**
   * Submit an image.
   *
   * This request message is responded to by either an ImageSubmissionOK, an InvalidSubmittedImage, or
   * an ImageSubmissionFailure(throwable) message.
   *
   * @param image the binary content of the image
   */
  case class SubmitImage(image: ImageContent)

  case class ImageSubmissionOK(tag: ImageTag)

  case object InvalidSubmittedImage

  case class UnexpectedFailure(error: Throwable)

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

