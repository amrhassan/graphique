package graphique.backends

import java.nio.file.Path

/**
 * Low-level IO implementations.
 */
trait IO {

  /**
   * Writes the raw data to the destination path.
   *
   * @param data raw data
   * @param dest path destination
   * @throws IOError
   */
  def write(dest: Path)(data: Array[Byte]): Unit

  /**
   * Checks whether the path points to an existing file.
   *
   * @param path the file path
   * @return the existence condition
   */
  def exists(path: Path): Boolean

  /**
   * Reads and returns all the bytes from the file pointed to by the given path, if found.
   *
   * @throws IOError
   */
  def read(path: Path): Option[Array[Byte]]

  /**
   * Deletes files in the given directory whose filenames are prefixed by the given prefix.
   *
   * @throws IOError
   */
  def delete(directory: Path, prefix: String): Unit
}