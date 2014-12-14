package graphique.backends.dummybackend

import java.net.InetSocketAddress
import java.nio.file.Paths
import java.util.concurrent.Executors

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}
import graphique.backends.{Content, RequestedImage, UrlProvider}

/**
 * A fully-functional dummy implementation of UrlProvider for testing.
 */
object DummyUrlProvider extends UrlProvider {

  val Port = 8192

  val httpServer = HttpServer.create(new InetSocketAddress(Port), 0)
  httpServer createContext("/", DummyHandler)
  httpServer setExecutor Executors.newFixedThreadPool(5)
  httpServer.start()

  object DummyHandler extends HttpHandler {
    def handle(exchange: HttpExchange): Unit = {
      val urlComponent = exchange.getRequestURI.getPath.substring(1)
      val UrlComponentPattern = """image/([^-]+).*""".r

      val tag = urlComponent match {
        case UrlComponentPattern(t) => t
      }

      if (!(DummyIO exists (DummyPaths ofRawImage tag))) {
        exchange.sendResponseHeaders(404, 0)
        exchange.close()
        return
      }

      try {
        val imageContent = DummyIO.read(Paths.get(urlComponent))
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
    if (DummyIO exists path)
      Some(s"http://localhost:$Port/$path")
    else
      None
  }

}
