package graphique.backends.localbackend

import akka.actor._
import graphique.backends.localbackend.imageserver.ImageServerManager
import graphique.backends.{Paths, UrlProvider, RequestedImage}

/**
 * The HTTP image server
 */
private[localbackend] class LocalUrlProvider(port: Int, paths: FilePaths, localIO: LocalIO) extends UrlProvider {
  require(port >= 0, "Port number must be positive")

  override def forRequestedImage(requestedImage: RequestedImage): Option[String] =
    if (haveRequestedImageInStock(requestedImage))
      Some(requestedImageUrl(requestedImage))
    else
      None

  private def haveRequestedImageInStock(requestedImage: RequestedImage): Boolean =
    localIO exists (paths ofRawImage requestedImage.tag)

  private def requestedImageUrl(requestedImage: RequestedImage): String = {
    val hashedAttributes = Paths hashImageAttributes requestedImage.attributes
    s"http://localhost:$port/${requestedImage.tag}-$hashedAttributes"
  }

  implicit val actorSystem = ActorSystem("LocalImageServerActorSystem")
  private val server = actorSystem actorOf(Props(new ImageServerManager(port, paths.imagePath)), "ImageServerManager")
  server ! ImageServerManager.Start
}
