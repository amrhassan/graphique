package graphique.images

import graphique.UnitSpec

class ImageValidatorTest extends UnitSpec {

  val validator = new ImageValidator

  test("Gets uncorrupted images right") {
    val uncorruptedImage = readResource("like_a_sir.jpg")
    assert(validator isValid uncorruptedImage)
  }

  test("Detects corrupted images") {
    val corruptedImage = readResource("invalid_image.jpg")
    assert((validator isValid corruptedImage) === false)
  }
}