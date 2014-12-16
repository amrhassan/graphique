package graphique

import akka.actor.Actor.Receive
import akka.actor.{ActorLogging, Actor, Props, ActorSystem}
import akka.io.IO
import com.typesafe.scalalogging.Logger
import graphique.backends.localbackend.LocalBackend
import graphique.backends.s3backend.S3Backend
import graphique.http.GraphiqueRest
import org.slf4j.LoggerFactory
import spray.can.Http

object App extends App {

  val log = Logger(LoggerFactory.getLogger("App"))
  val config = XdgConfigFactory.load().getConfig("graphique")

  implicit val actorSystem = ActorSystem("ROOT")

  val backend = config.getString("backend") match {
    case "localbackend" => LocalBackend(config.getConfig("localbackend"))
    case "s3backend" => S3Backend(config.getConfig("s3backend"))
  }

  log info s"Using the backend: ${backend.getClass.getSimpleName}"
  val graphique = actorSystem actorOf Props(new GraphiqueService(backend))
  val restService = actorSystem actorOf Props(new GraphiqueRest(graphique))

  val senderActor = actorSystem actorOf Props(new Actor with ActorLogging {
    def receive: Receive = {
      case Http.Bound(_) => log info s"REST service launched"
    }
  })

  IO(Http) tell(
    Http.Bind(restService, interface = config.getString("rest.interface"), port = config.getInt("rest.port")),
    senderActor
  )

  actorSystem.awaitTermination()
}
