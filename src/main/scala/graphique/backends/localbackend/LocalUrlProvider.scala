package graphique.backends.localbackend

import akka.actor._
import akka.io.{IO, Tcp}
import graphique.backends.{Paths, Content, UrlProvider, RequestedImage}
import net.sf.jmimemagic.Magic
import spray.can.Http
import spray.http._

import scala.util.{Failure, Success, Try}

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
  private val server = actorSystem actorOf(Props(new HttpServer), "HttpServer")
  server ! HttpServer.Start

  private class HttpServer extends Actor with ActorLogging {
    import HttpServer._

    var listener: ActorRef = Actor.noSender

    override def receive: Actor.Receive = {

      case Start =>
        listener = context actorOf(Props(new Listener), "Listener")
        IO(Http) ! Http.Bind(listener, interface = "localhost", port = port)

      case Stop =>
        IO(Http) ! Http.Unbind

      case Http.CommandFailed(command: Http.Bind) =>
        throw new IllegalStateException(s"Failed to launch a local HTTP image server on port $port")

      case Http.Bound(_) => ()

      case message =>
        log warning s"Received unexpected message: $message"
    }
  }

  private object HttpServer {

    /**
     * Instructs the HttpServer to start serving the images.
     */
    case object Start

    /**
     * Instructs the HttpServer to shut down.
     */
    case object Stop
  }

  private class Listener extends Actor with ActorLogging {
    override def receive: Actor.Receive = {

      case Http.Connected(remoteAddress, localAddress) =>
        val requestHandler =
          context actorOf Props(new RequestHandler)
        sender ! Http.Register(requestHandler)


      case message => log warning s"Unexpected message: $message"
    }
  }

  private class RequestHandler extends Actor with ActorLogging {

    import RequestHandler._

    override def receive: Actor.Receive = {

      case HttpRequest(HttpMethods.GET, Uri.Path(path), _, _, _) =>
        val imagePath(id) = path
        val filePath = paths ofUrlComponent id
        if (!(localIO exists filePath))
          sender ! notFoundResponse
        else {
          val response = Try {
            val image = localIO read filePath
            val mime = Content detectMimeType image
            (mime, image)
          } match {
            case Success((Some(mime), image)) =>
              HttpResponse(entity = HttpEntity(ContentType(MediaType.custom(mime)), image))
            case Failure(_) => errorResponse
            case _ => errorResponse
          }
          sender ! response
        }

      case Tcp.PeerClosed =>
        context stop self

      case Timedout =>
        context stop self

      case Http.ConfirmedClosed =>
        context stop self

      case Http.Closed =>
        context stop self

      case message =>
        log warning s"Unexpected message: $message"
        context stop self
    }
  }

  object RequestHandler {

    private val notFoundResponse = HttpResponse(status = StatusCodes.NotFound)
    private val errorResponse = HttpResponse(status = StatusCodes.InternalServerError)

    private val imagePath = """/([^/]+)/?""".r

    private val magic = new Magic
  }
}
