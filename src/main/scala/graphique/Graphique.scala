package graphique

import akka.actor.{ActorLogging, Actor}
import graphique.backends.{RequestedImage, ImageManager, RawImageManager}
import graphique.image.ImageValidator

/**
 * The entry point to the Graphique microservice.
 */
class Graphique(rawImages: RawImageManager, images: ImageManager) extends Actor with ActorLogging {

  import Graphique._

  private val imageValidator = new ImageValidator

  override def receive: Receive = {

    case SubmitImage(image, tag) =>
      log info s"Submitting an ${image.length}B image with the tag: $tag"

      if (imageValidator isValid image) {
        images.cache clearTaggedWith tag
        rawImages store(tag, image)
        sender ! ImageSubmissionOK
      } else {
        sender ! InvalidSubmittedImage
      }

    case RequestImageUrl(tag, attributes) =>
      log info s"Requesting the image $tag with attributes $attributes"
      val urlOption = images imageUrl(RequestedImage(tag, attributes), rawImages read tag)
      urlOption match {
        case Some(url) => sender ! RequestedImageUrl(url)
        case None => sender ! RequestedImageNotFound
      }
  }
}

object Graphique {

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
  case class RequestImageUrl(tag: String, attributes: image.ImageAttributes)

  case object RequestedImageNotFound

  case class RequestedImageUrl(url: String)
}

