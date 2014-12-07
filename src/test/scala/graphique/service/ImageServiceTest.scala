package graphique.service

import akka.actor.Props
import graphique.ActorSpec
import graphique.backends.{ImageManager, RawImageManager, RequestedImageCache}
import graphique.images.ImageAttributes

class ImageServiceTest extends ActorSpec {

  import graphique.service.ImageService._

  test("Submitting a valid image") {

    val rawImages = stub[RawImageManager]
    val images = stub[ImageManager]

    (images.cache _).when().returns(stub[RequestedImageCache])

    val graphique = system actorOf Props(new ImageService(rawImages, images))

    graphique ! SubmitImage(readResource("like_a_sir.jpg"), "sir")
    expectMsg(ImageSubmissionOK)
  }

  test("Submitting an invalid image") {

    val rawImages = stub[RawImageManager]
    val images = stub[ImageManager]

    (images.cache _).when().returns(stub[RequestedImageCache])

    val graphique = system actorOf Props(new ImageService(rawImages, images))

    graphique ! SubmitImage(readResource("invalid_image.jpg"), "normal_image")
    expectMsg(InvalidSubmittedImage)
  }

  test("Requesting an image URL") {
    val rawImages = stub[RawImageManager]
    val images = stub[ImageManager]

    val url = "http://localhost/image.png"
    (images.imageUrl _).when(*, *).returns(Some(url))

    val graphique = system actorOf Props(new ImageService(rawImages, images))

    graphique ! RequestImageUrl("my-face", ImageAttributes.originalImage)
    expectMsg(RequestedImageUrl(url))
  }

  test("Requesting an image URL for an unavailable image") {
    val rawImages = stub[RawImageManager]
    val images = stub[ImageManager]

    (images.imageUrl _).when(*, *).returns(None)

    val graphique = system actorOf Props(new ImageService(rawImages, images))

    graphique ! RequestImageUrl("my-face", ImageAttributes.originalImage)
    expectMsg(RequestedImageNotFound)
  }
}