package graphique.backends

import java.nio.file
import graphique.UnitSpec
import org.apache.commons.lang3.RandomStringUtils

class PathsTest extends UnitSpec {

  val paths = new Paths(file.Paths.get(""))

  it should "be consistent path of images" in {

    val imageId = RandomStringUtils.randomAlphanumeric(20)
    val path = paths ofImage imageId

    val newId = imageId + ""
    imageId should not be theSameElementsAs(newId)

    (paths ofImage newId) should be (path)
  }
}
