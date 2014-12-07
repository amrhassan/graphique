package graphique

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}

/**
 * Base class for Akka unit tests.
 */
abstract class ActorSpec extends TestKit(ActorSystem("TestActorSystem")) with ImplicitSender with TestSpec {

  override protected def afterAll(): Unit = {
    super.afterAll()
    system shutdown()
  }
}