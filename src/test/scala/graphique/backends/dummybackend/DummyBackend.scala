package graphique.backends.dummybackend

import graphique.backends._

/**
 * A fully functional backend that stores the file content in memory.
 */
object DummyBackend {

  def apply(): Backend = {
    val io = new DummyIO
    val urls = new DummyUrlProvider(io, DummyPaths)
    val images = new ImageManager(io, DummyPaths)

    new Backend(images, urls)
  }
}