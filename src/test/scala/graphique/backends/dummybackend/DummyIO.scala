package graphique.backends.dummybackend

import java.nio.file
import graphique.backends.IO
import scala.collection.mutable

/**
 * A fully-functional dummy IO implementation for testing.
 */
class DummyIO extends IO {

  var data = mutable.Map.empty[file.Path, Array[Byte]]

  def read(path: file.Path): Array[Byte] = data(path)

  def exists(path: file.Path): Boolean = data contains path

  def delete(directory: file.Path, prefix: String): Unit =
    data = data filterNot (pair => pair._1.startsWith(prefix))

  def write(dest: file.Path)(data: Array[Byte]): Unit = this.data(dest) = data
}