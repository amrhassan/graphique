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

    val randomTag = RandomStringUtils.randomAlphanumeric(20)
    val attributes = images.ImageAttributes.originalImage.resizedTo(Dimensions(42, 22)).transcodedTo(JPEGFormat(0.3))

    val path = paths ofImage RequestedImage(randomTag, attributes)

    val newTag = randomTag + ""
    newTag should not be theSameElementsAs(randomTag)

    val newAttributes = attributes.copy()
    attributes should not be theSameInstanceAs(newAttributes)

    (paths ofImage RequestedImage(newTag, newAttributes)) should be (path)
  }

  it should "differentiate the paths of images with different attributes" in {

    val randomTag = RandomStringUtils.randomAlphanumeric(20)
    val attributes = images.ImageAttributes.originalImage.resizedTo(Dimensions(42, 22)).transcodedTo(PNGFormat)
    val differentAttributes = attributes.transcodedTo(JPEGFormat(0.3))

    val path = paths ofImage RequestedImage(randomTag, attributes)

    assert(path !== (paths ofImage RequestedImage(randomTag, differentAttributes)))
  }
}
