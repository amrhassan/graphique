package graphique.backends.localbackend

import akka.actor._
import graphique.backends.localbackend.imageserver.ImageServerManager
import graphique.backends._

/**
 * The HTTP image server
 */
private[localbackend] class LocalUrlProvider(hostname: String, port: Int, paths: Paths) extends UrlProvider {
  require(port >= 0, "Port number must be positive")

  def apply(imageId: ImageId): String = s"http://$hostname:$port/$imageId"

  implicit val actorSystem = ActorSystem("LocalImageServerActorSystem")
  private val server =
    actorSystem actorOf(Props(new ImageServerManager(hostname, port, paths.pathPrefix)), "ImageServerManager")
  server ! ImageServerManager.Start
}
