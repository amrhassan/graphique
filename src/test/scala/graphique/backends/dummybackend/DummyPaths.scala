package graphique.backends.dummybackend

import java.nio.file
import graphique.backends.Paths

/**
 * A fully-functional dummy implementation of Paths for testing.
 */
object DummyPaths extends Paths(file.Paths.get("raw"), file.Paths.get("image"))
