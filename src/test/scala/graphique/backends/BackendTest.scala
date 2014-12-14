package graphique.backends


import graphique.TestSpec
import graphique.backends.dummybackend.{DummyBackend, DummyUrlProvider, DummyPaths, DummyIO}
import graphique.images.ImageAttributes

class BackendTest extends TestSpec {

  val backend = DummyBackend()

  "Backend" should "reject corrupted image" in {
    intercept[Backend.InvalidImageError] {
      val image = readResource("invalid_image.jpg")
      backend submitImage ("sup", image)
    }
  }

  it should "accept a valid image" in {
    val image = readResource("like_a_sir.jpg")
    backend submitImage("like_a_sir", image)
  }

  it should "have a URL for a submitted image" in {
    val image = readResource("like_a_sir.jpg")
    backend submitImage("like_a_sir", image)
    val url = backend imageUrlFor("like_a_sir", ImageAttributes.originalImage)
    url should not be None
  }

  it should "not have a URL for an unsubmitted image" in {
    pending
  }

  it should "respond with a JPEG image URL when requested" in {
    pending
  }

  it should "respond with a PNG image URL when requested" in {
    pending
  }

  it should "respond with an image within the requested image size" in {
    pending
  }
}