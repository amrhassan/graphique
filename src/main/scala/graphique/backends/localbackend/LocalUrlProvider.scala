package graphique.backends.localbackend

import akka.actor._
import graphique.backends.localbackend.imageserver.ImageServerManager
import graphique.backends._

/**
 * The HTTP image server
 */
private[localbackend] class LocalUrlProvider(port: Int, paths: FilePaths) extends UrlProvider {
  require(port >= 0, "Port number must be positive")

  override def forImage(id: ImageId): String = s"http://localhost:$port/$id"

  implicit val actorSystem = ActorSystem("LocalImageServerActorSystem")
  private val server = actorSystem actorOf(Props(new ImageServerManager(port, paths.imagePath)), "ImageServerManager")
  server ! ImageServerManager.Start
}
