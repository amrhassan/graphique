package graphique.backends


import javax.imageio.ImageIO

import com.mashape.unirest.http.Unirest
import graphique.TestSpec
import graphique.backends.dummybackend.DummyBackend
import graphique.images.{Dimensions, PNGFormat, JPEGFormat, ImageAttributes}


class BackendTest extends TestSpec {

  /**
   * Executes test code with a single Backend instance isolated from the others.
   */
  def withBackend(test: Backend => Unit) = test(DummyBackend())

  "A Backend" should "reject corrupted image" in {
    withBackend { backend =>
      intercept[InvalidImageError] {
        val image = readResource("invalid_image.jpg")
        backend submitImage image
      }
    }
  }

  it should "accept a valid image" in {
    withBackend { backend =>
      val image = readResource("like_a_sir.jpg")
      backend submitImage image
    }
  }

  it should "have a URL for a submitted image" in {
    withBackend { backend =>
      val image = readResource("like_a_sir.jpg")
      val tag = backend submitImage image
      backend imageUrlFor (tag, ImageAttributes.originalImage)
    }
  }

  it should "create a variation of an image it has" in {
    withBackend { backend =>
      val image = readResource("like_a_sir.jpg")
      val tag = backend submitImage image
      backend createImage (tag, ImageAttributes.originalImage.resizedToWithin(Dimensions(100, 100)))
    }
  }

  it should "respond with a JPEG image URL when requested" in {
    withBackend { backend =>
      val tag = backend submitImage readResource("ayam_soda.png")
      val url = backend urlForExistingImage (tag, ImageAttributes.originalImage.transcodedTo(JPEGFormat()))
      url should not be None
      val response = gulp(Unirest.get(url).asBinary().getBody)
      (Content detectMimeType response.toArray) should be(Some("image/jpeg"))
    }
  }

  it should "respond with a PNG image URL when requested" in {
    withBackend { backend =>
      val tag = backend submitImage readResource("like_a_sir.jpg")
      val url = backend urlForExistingImage (tag, ImageAttributes.originalImage.transcodedTo(PNGFormat))
      url should not be None
      val response = gulp(Unirest.get(url).asBinary().getBody)
      (Content detectMimeType response.toArray) should be(Some("image/png"))
    }
  }

  it should "respond with an image within the requested image size" in {
    withBackend { backend =>
      val tag = backend submitImage readResource("like_a_sir.jpg")
      val url = backend urlForExistingImage (tag, ImageAttributes.originalImage.resizedToWithin(Dimensions(100, 100)))
      url should not be None
      val responseImage = ImageIO.read(Unirest.get(url).asBinary().getBody)
      responseImage.getHeight should be(100)
      responseImage.getWidth should be(92 +- 2)
    }
  }
}