package graphique.localbackend

import java.nio.file.{Files, Path}

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
   */
  def writeData(data: Array[Byte], dest: Path): Unit = {
    val parent = dest.getParent
    if (!exists(parent))
      createDirectories(parent)
    Files.write(dest, data)
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
   */
  def createDirectories(directory: Path): Unit = {
    Files.createDirectories(directory)
  }
}
