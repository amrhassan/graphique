package graphique.images

import graphique.UnitSpec

class ImageValidatorTest extends UnitSpec {

  val validator = new ImageValidator

  it should "get uncorrupted images right" in {
    val uncorruptedImage = readResource("like_a_sir.jpg")
    assert(validator isValid uncorruptedImage)
  }

  it should "detect corrupted images" in {
    val corruptedImage = readResource("invalid_image.jpg")
    assert((validator isValid corruptedImage) === false)
  }
}