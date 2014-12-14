package graphique.backends.s3backend

import java.nio.file.Paths
import graphique.backends

private[s3backend] class S3Paths extends backends.Paths(
  rawImagePath = Paths.get("raw"),
  imagePath = Paths.get("image")
)