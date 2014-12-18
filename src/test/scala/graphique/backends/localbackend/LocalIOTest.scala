package graphique.backends.localbackend

import java.nio.file.{Path, Files, Paths}

import graphique.TestSpec

import scala.util.Random

class LocalIOTest extends TestSpec {

  val io = new LocalIO

  "A LocalIO" should "write correctly" in {
    val path = tempFile
    (io write path)("Hello".getBytes)
    gulpString(path) should be("Hello")
  }

  it should "read correctly" in {
    val path = tempFile
    Files.write(path, "Hi".getBytes)
    String.valueOf((io read path) map (_.toChar)) should be("Hi")
  }

  it should "check files for existence correctly" in {
    val path = tempFile
    Files.write(path, "Sup".getBytes)
    (io exists path) should be(true)
  }

  it should "delete files according to their scheme directly" in {
    val dir = tempDirectory
    val filePaths = for (prefix <- Seq("prefix1", "prefix1", "prefix2", "prefix3"))
      yield dir resolve (prefix + "rest_of_file_name" + Random.nextString(42))

    filePaths foreach (Files.createFile(_))

    io delete(dir, "prefix1")

    val undeletedFilePaths = filePaths drop 2

    undeletedFilePaths foreach { path =>
      Files.exists(path) should be(true)
    }
  }
}