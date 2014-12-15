package graphique.backends.localbackend.imageserver

import java.nio.file.Path

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.io.IO
import spray.can.Http

private[localbackend] class ImageServerManager(hostname: String, port: Int, imagePath: Path)
  extends Actor with ActorLogging {

  require(port >= 0)

  import graphique.backends.localbackend.imageserver.ImageServerManager._

  var imageServer: Option[ActorRef] = None
  var sprayCanHttpListener: Option[ActorRef] = None

  def receive: Receive = {

    case Start =>

      // Stop any running server first
      sprayCanHttpListener foreach { listener =>
        listener ! Http.Unbind
      }

      // Create a new managed ImageServer instance
      sprayCanHttpListener = None
      imageServer = Some(context actorOf(Props(new ImageServer(imagePath)), "Listener"))
      IO(Http)(context.system) ! Http.Bind(imageServer.get, interface = hostname, port = port)

    case Stop =>
      IO(Http)(context.system) ! Http.Unbind

    case Http.CommandFailed(command: Http.Bind) =>
      throw new IllegalStateException(s"Failed to launch a local HTTP image server on port $port")

    case Http.Bound(_) =>
      sprayCanHttpListener = Some(sender) // The sender is used later for stopping

    case message =>
      log warning s"Received unexpected message: $message"
  }
}

private[localbackend] object ImageServerManager {

  /**
   * Instructs the HttpServer to start serving the images.
   */
  case object Start

  /**
   * Instructs the HttpServer to shut down.
   */
  case object Stop
}