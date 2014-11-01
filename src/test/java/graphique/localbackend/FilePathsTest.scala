package graphique.localbackend

import java.nio.file.Paths

import org.scalatest.FunSuite

class FilePathsTest extends FunSuite {

  trait Context {
    val filePaths = new FilePaths(Paths.get("/images"))
  }

  test("raw image path") {
    new Context {
      assert(filePaths.ofRawImage("image1") === Paths.get("/images/image1"))
    }
  }

  test("thubnailed image path") {
    new Context {
      assert(filePaths.ofThumbnail("zimago", 42, 23) === Paths.get("/images/42x23/zimago"))
    }
  }
}
