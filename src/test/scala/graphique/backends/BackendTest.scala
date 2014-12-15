package graphique.backends


import javax.imageio.ImageIO

import com.mashape.unirest.http.Unirest
import graphique.TestSpec
import graphique.backends.dummybackend.{DummyBackend, DummyUrlProvider, DummyPaths, DummyIO}
import graphique.images.{Dimensions, PNGFormat, JPEGFormat, ImageAttributes}

import scala.io.{Codec, Source}

class BackendTest extends TestSpec {

  /**
   * Executes test code with a single Backend instance isolated from the others.
   */
  def withBackend(test: Backend => Unit) = test(DummyBackend())

  "Backend" should "reject corrupted image" in {
    withBackend { backend =>
      intercept[Backend.InvalidImageError] {
        val image = readResource("invalid_image.jpg")
        backend submitImage("sup", image)
      }
    }
  }

  it should "accept a valid image" in {
    withBackend { backend =>
      val image = readResource("like_a_sir.jpg")
      backend submitImage("like_a_sir", image)
    }
  }

  it should "have a URL for a submitted image" in {
    withBackend { backend =>
      val image = readResource("like_a_sir.jpg")
      backend submitImage("like_a_sir", image)
      val url = backend imageUrlFor("like_a_sir", ImageAttributes.originalImage)
      url should not be None
    }
  }

  it should "not have a URL for an unsubmitted image" in {
    withBackend { backend =>
      (backend imageUrlFor "like_a_sir") should be(None)
    }
  }

  it should "respond with a JPEG image URL when requested" in {
    withBackend { backend =>
      backend submitImage("ayam_soda", readResource("ayam_soda.png"))
      val url = backend imageUrlFor("ayam_soda", ImageAttributes.originalImage.transcodedTo(JPEGFormat()))
      url should not be None
      val response = gulp(Unirest.get(url.get).asBinary().getBody)
      (Content detectMimeType response.toArray) should be(Some("image/jpeg"))
    }
  }

  it should "respond with a PNG image URL when requested" in {
    withBackend { backend =>
      backend submitImage("sir", readResource("like_a_sir.jpg"))
      val url = backend imageUrlFor("sir", ImageAttributes.originalImage.transcodedTo(PNGFormat))
      url should not be None
      val response = gulp(Unirest.get(url.get).asBinary().getBody)
      (Content detectMimeType response.toArray) should be(Some("image/png"))
    }
  }

  it should "respond with an image within the requested image size" in {
    withBackend { backend =>
      backend submitImage("sir", readResource("like_a_sir.jpg"))
      val url = backend imageUrlFor("sir", ImageAttributes.originalImage.resizedTo(Dimensions(100, 100)))
      url should not be None
      val responseImage = ImageIO.read(Unirest.get(url.get).asBinary().getBody)
      responseImage.getHeight should be(100)
      responseImage.getWidth should be(92 +- 2)
    }
  }
}