package graphique

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.io.IO
import com.typesafe.scalalogging.Logger
import graphique.backends.localbackend.LocalBackend
import graphique.backends.s3backend.S3Backend
import graphique.http.GraphiqueRest
import graphique.util.XdgConfigFactory
import org.slf4j.LoggerFactory
import spray.can.Http

object Application extends App {

  val log = Logger(LoggerFactory.getLogger("App"))
  val config = XdgConfigFactory.load().getConfig("graphique")

  implicit val actorSystem = ActorSystem("ROOT")

  val backend = config.getString("backend") match {
    case "localbackend" => LocalBackend(config.getConfig("localbackend"))
    case "s3backend" => S3Backend(config.getConfig("s3backend"))
  }

  log info s"Using the backend: ${backend.getClass.getSimpleName}"

  val graphique =
    actorSystem actorOf(Props(new GraphiqueService(backend, config.getInt("threadPoolSize"))), "graphique")

  val restService =
    actorSystem actorOf(Props(new GraphiqueRest(graphique, config.getInt("threadPoolSize"))), "graphique-rest")

  val senderActor = actorSystem actorOf Props(new Actor with ActorLogging {
    def receive: Receive = {
      case Http.Bound(_) => log info s"REST service launched"
      case Http.CommandFailed(_) =>
        log error "Failed to launch REST service"
        context.system.shutdown()
      case m =>
        log warning s"Unexpected message: $m"
        context.system.shutdown()
    }
  })

  IO(Http) tell(
    Http.Bind(restService, interface = config.getString("rest.interface"), port = config.getInt("rest.port")),
    senderActor
  )

  actorSystem.awaitTermination()
}
