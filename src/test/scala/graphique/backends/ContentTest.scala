package graphique.backends

import graphique.UnitSpec

class ContentTest extends UnitSpec {

  "detectMimeTypeE" should "detect mime types of images correctly" in {
    (Content detectMimeType readResource("like_a_sir.jpg")).get should be("image/jpeg")
    (Content detectMimeType readResource("ayam_soda.png")).get should be("image/png")
  }
}