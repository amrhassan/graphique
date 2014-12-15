package graphique.backends.localbackend.imageserver


import java.io.IOException
import java.nio.file.Path

import graphique.backends.Content
import spray.http.{MediaType, ContentType}
import spray.routing.directives.ContentTypeResolver

import scala.io.{Codec, Source}

private[localbackend] class MimeMagicOrExtensionContentTypeResolver(directory: Path) extends ContentTypeResolver {

  val fallback: ContentTypeResolver = ContentTypeResolver.Default

  def apply(fileName: String): ContentType = {
    try {
      val fullPath = directory resolve fileName
      val bytes = Source.fromFile(fullPath.toString)(Codec.ISO8859) map (_.toByte)
      (Content detectMimeType bytes.toArray) map { mimeType =>
        ContentType(MediaType.custom(mimeType))
      } getOrElse {
        fallback(fileName)
      }
    } catch {
      case e: IOException => fallback(fileName)
    }
  }
}