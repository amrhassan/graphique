package graphique.backends.localbackend

import java.io.IOException
import java.nio.file._

import graphique.backends.abstractbackend.IOError

/**
 * Low-level IO operations.
 */
private[localbackend] class IO {

  /**
   * Writes the raw data to the local file destination. If the file destination
   * is in a non-existing directory, all the leading directories will be created first.
   *
   * @param data raw data
   * @param dest file destination
   * @throws IOError
   */
  def writeData(data: Array[Byte], dest: Path): Unit = {
    val parent = dest.getParent

    try {
      if (!exists(parent))
        createDirectories(parent)
      Files.write(dest, data)
    } catch {
      case e: IOException => throw new IOError(e)
    }
  }

  /**
   * Checks whether a local filesystem path points to an existing file.
   *
   * @param path the file path
   * @return the existence condition
   */
  def exists(path: Path): Boolean = Files.exists(path)

  /**
   * Creates the directory at the given path and all the directories leading to it.
   *
   * @param directory the directory path
   * @throws IOError
   */
  def createDirectories(directory: Path): Unit = {
    try {
      Files.createDirectories(directory)
    } catch {
      case e: IOException => throw new IOError(e)
      case e: FileAlreadyExistsException => throw new IOError(e)
    }
  }

  /**
   * Reads and returns all the bytes from the file pointed to by the given path.
   *
   * @throws IOError
   */
  def readData(path: Path): Array[Byte] =
    try {
      Files.readAllBytes(path)
    } catch {
      case e: IOException => throw new IOError(e)
    }

  /**
   * Delete files matched by the given glob in the given directory path, if any existed.
   *
   * Example:
   *  deleteFiles(Paths.get("/home/amr/files), "log-*")
   *
   * @param directory
   * @param glob
   * @throws IOError
   */
  def deleteFiles(directory: Path, glob: String): Unit = {
    try {
      import scala.collection.JavaConversions._
      Files.newDirectoryStream(directory, glob) foreach Files.delete
    } catch {
      case _: NoSuchFileException => ()
      case _: NotDirectoryException => ()
      case e: IOException => throw new IOError(e)
    }
  }
}
