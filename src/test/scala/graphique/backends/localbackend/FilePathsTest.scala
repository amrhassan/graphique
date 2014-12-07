package graphique.backends.localbackend

import java.nio.file.Paths
import graphique.backends.RequestedImage
import graphique.images
import graphique.images.{Dimensions, PNGFormat, JPEGFormat}
import org.apache.commons.lang3.RandomStringUtils
import org.scalatest.FunSuite

class FilePathsTest extends FunSuite {

  val filePaths = new FilePaths(Paths.get("/images"))

  test("consistent path of raw images") {

    val randomTag = RandomStringUtils.randomAlphanumeric(20)
    val path = filePaths ofRawImage randomTag

    // Checking a hundred times for consistency. Sounds like enough?
    for (_ <- 1 to 100) {
      val newTag = randomTag + ""     // Same content but different object
      assert(newTag ne randomTag)     // Assert it's not the same tag object

      // Assert that a different tag object with the same content yields the same path
      assert(path === (filePaths ofRawImage (randomTag + "")))
    }
  }

  test("consistent path of images") {

    val randomTag = RandomStringUtils.randomAlphanumeric(20)
    val attributes = images.ImageAttributes.originalImage.resizedTo(Dimensions(42, 22)).transcodedTo(JPEGFormat(0.3))

    val path = filePaths ofImage RequestedImage(randomTag, attributes)

    for (_ <- 1 to 100) {
      val newTag = randomTag + ""
      assert(randomTag ne newTag)

      val newAttributes = attributes.copy()
      assert(newAttributes ne attributes)

      assert(path === (filePaths ofImage RequestedImage(newTag, newAttributes)))
    }
  }

  test("different path of images with different attributes") {

    val randomTag = RandomStringUtils.randomAlphanumeric(20)
    val attributes = images.ImageAttributes.originalImage.resizedTo(Dimensions(42, 22)).transcodedTo(PNGFormat)
    val differentAttributes = attributes.transcodedTo(JPEGFormat(0.3))

    val path = filePaths ofImage RequestedImage(randomTag, attributes)

    assert(path !== (filePaths ofImage RequestedImage(randomTag, differentAttributes)))
  }
}