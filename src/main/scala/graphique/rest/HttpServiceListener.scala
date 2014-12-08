package graphique.rest

import akka.actor.ActorLogging
import spray.routing._
import spray.util.LoggingContext


abstract class HttpServiceListener extends HttpServiceActor with ActorLogging {

  implicit val exceptionHandler = ExceptionHandler.default
  implicit val rejectionHandler = RejectionHandler.Default
  implicit val routingSettings = RoutingSettings.default
  implicit val loggingContext = LoggingContext.fromActorRefFactory

  def route: Route

  override def receive: Receive = runRoute(route)

}