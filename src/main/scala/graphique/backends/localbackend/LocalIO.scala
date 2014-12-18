package graphique.backends.localbackend

import java.io.IOException
import java.nio.file._

import graphique.backends.IO

/**
 * IO implementation that stores uses the local filesystem for storage.
 */
private[localbackend] class LocalIO extends IO {

  import graphique.backends._

  def write(dest: Path)(data: Array[Byte]): Unit = {
    val parent = dest.getParent

    try {
      if (!exists(parent))
        createDirectories(parent)
      Files.write(dest, data)
    } catch {
      case e: IOException => throw new IOError(e)
    }
  }

  def exists(path: Path): Boolean = Files.exists(path)

  /**
   * Creates the directory at the given path and all the directories leading to it.
   *
   * @param directory the directory path
   * @throws IOError
   */
  private def createDirectories(directory: Path): Unit = {
    try {
      Files.createDirectories(directory)
    } catch {
      case e: IOException => throw new IOError(e)
      case e: FileAlreadyExistsException => throw new IOError(e)
    }
  }

  def read(path: Path): Option[Array[Byte]] =
    try {
      Some(Files.readAllBytes(path))
    } catch {
      case e: IOException => throw new IOError(e)
    }

  def delete(directory: Path, prefix: String): Unit = {
    try {
      import scala.collection.JavaConversions._
      Files.newDirectoryStream(directory, s"$prefix*") foreach Files.delete
    } catch {
      case _: NoSuchFileException => ()
      case _: NotDirectoryException => ()
      case e: IOException => throw new IOError(e)
    }
  }
}
