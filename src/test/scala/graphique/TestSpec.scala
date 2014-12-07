package graphique

import java.io.File
import java.nio.file.Files

import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

/**
 * Base trait for unit tests.
 */
trait TestSpec extends FunSuiteLike with BeforeAndAfterAll with MockFactory {

  /**
   * Reads resource file content and returns it.
   */
  def readResource(name: String): Array[Byte] = {
    val file = new File((ClassLoader getSystemResource name).getFile)
    Files.readAllBytes(file.toPath)
  }
}