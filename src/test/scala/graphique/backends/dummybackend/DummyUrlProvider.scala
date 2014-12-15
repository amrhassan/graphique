package graphique.backends.dummybackend

import java.io.{IOException, IOError}
import java.net.{ServerSocket, InetSocketAddress}
import java.util.concurrent.Executors

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}
import graphique.backends._

import scala.util.Random

/**
 * A fully-functional dummy implementation of UrlProvider for testing.
 */
class DummyUrlProvider(io: IO, paths: Paths) extends UrlProvider {

  val Port = {
    val possible = Random.shuffle(8000 to 8999).toStream
    val available = possible filter { port =>
      try {
        val server = Option(new ServerSocket(port))
        server foreach (_.close())
        true
      } catch {
        case e: IOException => false
        case e: Throwable => e.printStackTrace() ; false
      }
    }
    available.head
  }

  val httpServer = HttpServer.create(new InetSocketAddress(Port), 0)
  httpServer createContext("/", DummyHandler)
  httpServer setExecutor Executors.newFixedThreadPool(1)
  httpServer.start()

  object DummyHandler extends HttpHandler {
    def handle(exchange: HttpExchange): Unit = {
      val imageId = exchange.getRequestURI.getPath.substring(1)

      if (!(io exists (DummyPaths ofImage imageId))) {
        exchange.sendResponseHeaders(404, 0)
        exchange.close()
        return
      }

      try {
        val imageContent = io read (paths ofImage imageId)
        exchange.getResponseHeaders.add("Content-Type", Content.detectMimeType(imageContent).get)
        exchange.sendResponseHeaders(200, imageContent.length)
        val body = exchange.getResponseBody
        body.write(imageContent)
        body.close()
      } catch {
        case e: Throwable => print(e)
      }
    }
  }

  def forImage(id: ImageId): String = s"http://localhost:$Port/$id"
}
