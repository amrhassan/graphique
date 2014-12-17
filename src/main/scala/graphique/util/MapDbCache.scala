package graphique.util

import spray.caching.Cache
import scala.concurrent.{ExecutionContext, Future}
import org.mapdb._
import spray.util._

class MapDbCache[V] extends Cache[V] {

  private val map = DBMaker.newTempTreeMap[Any, V]()

  import ExecutionContext.Implicits.global

  def apply(key: Any, genValue: () => Future[V])(implicit ec: ExecutionContext): Future[V] = {
    Future {
      if (map.containsKey(key)) {
        map.get(key)
      } else {
        val value = genValue().await
        map.put(key, value)
        value
      }
    }
  }

  def get(key: Any): Option[Future[V]] = Option(Future(map get key))

  def clear(): Unit = map clear()

  def size: Int = map.size

  def remove(key: Any): Option[Future[V]] = Some(Future(map remove key))

  def keys: Set[Any] = {
    import scala.collection.JavaConversions.asScalaSet
    asScalaSet(map.keySet()).toSet
  }

  def ascendingKeys(limit: Option[Int]): Iterator[Any] = {
    import scala.collection.JavaConversions.iterableAsScalaIterable
    val iterable = iterableAsScalaIterable(map.navigableKeySet)
    if (limit.isDefined)
      iterable.take(limit.get).iterator
    else
      iterable.iterator
  }
}
