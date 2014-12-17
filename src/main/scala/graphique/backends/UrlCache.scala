package graphique.backends

import java.nio.file.{Files, Path}
import org.mapdb.DBMaker
import scala.collection.mutable
import scala.collection.JavaConversions._

trait UrlCache {

  def hasUrlFor(requestedImage: RequestedImage): Boolean

  def getUrlFor(requestedImage: RequestedImage): Option[String]

  def getUrlForOrElseUpdate(requestedImage: RequestedImage, url: => Option[String]) : Option[String]

  def clearTaggedWith(tag: ImageTag): Unit
}

object UrlCache {

  def mapDb(storageLocation: Path): UrlCache = new MapDbUrlCache(storageLocation)

  def noCache = IdentityUrlCache
}

/**
 * An on-disk cache backed by MapDB.
 *
 * @param storageLocation the location where the database is stored.
 */
class MapDbUrlCache(storageLocation: Path) extends UrlCache {

  private type CacheMap = mutable.Map[ImageTag, Map[RequestedImage, Option[String]]]

  private val db = {
    val directory = storageLocation.getParent
    if (!Files.isDirectory(directory))
      Files.createDirectories(directory)
    DBMaker.newFileDB(storageLocation.toFile).closeOnJvmShutdown.make()
  }

  private val cacheMap: CacheMap = mapAsScalaMap(db.getHashMap("urls"))

  def hasUrlFor(requestedImage: RequestedImage): Boolean = {
    (tagged(requestedImage.tag) getOrElse Map.empty) contains requestedImage
  }

  def getUrlFor(requestedImage: RequestedImage): Option[String] = {
    (tagged(requestedImage.tag) getOrElse Map.empty) getOrElse(requestedImage, None)
  }

  def getUrlForOrElseUpdate(requestedImage: RequestedImage, url: => Option[String]) : Option[String] = {
    this.synchronized {
      val group = tagged(requestedImage.tag) getOrElse Map.empty
      if (group contains requestedImage) {
        (group get requestedImage).get
      } else {
        val urlValue = url
        cacheMap(requestedImage.tag) = group updated(requestedImage, urlValue)
        db.commit()
        // TODO: Maybe defrag every N calls to db.commit()?
        urlValue
      }
    }
  }

  def clearTaggedWith(tag: ImageTag): Unit = {
    this.synchronized {
      cacheMap.remove(tag)
      db.commit()
    }
  }

  private def tagged(tag: ImageTag): Option[Map[RequestedImage, Option[String]]] = cacheMap get tag
}

/**
 * The UrlCache to use when you don't want URL caching.
 */
object IdentityUrlCache extends UrlCache {

  def hasUrlFor(requestedImage: RequestedImage): Boolean = false

  def clearTaggedWith(tag: ImageTag): Unit = {}

  def getUrlForOrElseUpdate(requestedImage: RequestedImage, url: => Option[String]): Option[String] = url

  def getUrlFor(requestedImage: RequestedImage): Option[String] = None
}