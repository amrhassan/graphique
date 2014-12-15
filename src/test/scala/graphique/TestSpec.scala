package graphique

import java.io.{InputStream, File}
import java.nio.file.{Path, Files}

import org.scalamock.scalatest.MockFactory
import org.scalatest._

import scala.io.{Codec, Source}

/**
 * Base trait for unit tests.
 */
trait TestSpec extends FlatSpecLike with ShouldMatchers with BeforeAndAfterAll with MockFactory {

  /**
   * Reads resource file content and returns it.
   */
  def readResource(name: String): Array[Byte] = {
    val file = new File((getClass.getClassLoader getResource name).getFile)
    Files.readAllBytes(file.toPath)
  }

  def gulp(in: InputStream): Array[Byte] =
    (Source.fromInputStream(in)(Codec.ISO8859) map (_.toByte)).toArray

  def gulp(path: Path): Array[Byte] = gulp(Files.newInputStream(path))

  def gulpString(path: Path): String = String.valueOf(gulp(path) map (_.toChar))

  def tempFile: Path = Files.createTempFile("graphique-test", "")

  def tempDirectory: Path = Files.createTempDirectory("graphique-test")
}