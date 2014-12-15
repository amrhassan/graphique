package graphique.backends.dummybackend

import java.net.InetSocketAddress
import java.nio.file.Paths
import java.util.concurrent.Executors

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}
import graphique.backends.{IO, Content, RequestedImage, UrlProvider}

import scala.util.Random

/**
 * A fully-functional dummy implementation of UrlProvider for testing.
 */
class DummyUrlProvider(io: IO) extends UrlProvider {

  val Port = Random.shuffle(8000 to 8999).head

  val httpServer = HttpServer.create(new InetSocketAddress(Port), 0)
  httpServer createContext("/", DummyHandler)
  httpServer setExecutor Executors.newFixedThreadPool(1)
  httpServer.start()

  object DummyHandler extends HttpHandler {
    def handle(exchange: HttpExchange): Unit = {
      val urlComponent = exchange.getRequestURI.getPath.substring(1)
      val UrlComponentPattern = """image/([^-]+).*""".r

      val tag = urlComponent match {
        case UrlComponentPattern(t) => t
      }

      if (!(io exists (DummyPaths ofRawImage tag))) {
        exchange.sendResponseHeaders(404, 0)
        exchange.close()
        return
      }

      try {
        val imageContent = io read Paths.get(urlComponent)
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

  def forRequestedImage(requestedImage: RequestedImage): Option[String] = {
    val path = DummyPaths ofImage requestedImage
    if (io exists path)
      Some(s"http://localhost:$Port/$path")
    else
      None
  }

}
