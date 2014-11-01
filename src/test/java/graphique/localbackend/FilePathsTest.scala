package graphique.localbackend

import java.nio.file.Paths

import org.scalatest.FunSuite

class FilePathsTest extends FunSuite {

  trait Context {
    val filePaths = new FilePaths(Paths.get("/images"))
  }

  test("raw image path") {
    new Context {
      assert(filePaths.ofRawImage("image1") === Paths.get("/images/raw/image1"))
    }
  }

  test("thumbnailed image path") {
    new Context {
      assert(filePaths.ofThumbnailImage("zimago", 42, 23) === Paths.get("/images/thumbnail/42x23/zimago"))
    }
  }

  test("test full size image path") {
    new Context {
      assert(filePaths.ofFullSizeImage("shlurb") === Paths.get("/images/fullsize/shlurb.jpg"))
    }
  }
}
