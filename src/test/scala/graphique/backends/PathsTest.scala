package graphique.backends

import java.nio.file

import graphique.images.{Dimensions, JPEGFormat, PNGFormat}
import graphique.{UnitSpec, images}
import org.apache.commons.lang3.RandomStringUtils

class PathsTest extends UnitSpec {

  val paths = new Paths(file.Paths.get("raw"), file.Paths.get("image"))

  it should "be consistent with path of raw images" in {

    val randomTag = RandomStringUtils.randomAlphanumeric(20)
    val path = paths ofRawImage randomTag

    val newTag = randomTag + ""     // Same content but different object
    randomTag should not be theSameInstanceAs(newTag)    // Assert it's not the same tag object

    // Assert that a different tag object with the same content yields the same path
    (paths ofRawImage (randomTag + "")) should be(path)
  }

  it should "be consistent path of images" in {

    val imageId = RandomStringUtils.randomAlphanumeric(20)
    val path = paths ofImage imageId

    val newId = imageId + ""
    imageId should not be theSameElementsAs(newId)

    (paths ofImage newId) should be (path)
  }
}
