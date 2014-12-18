package graphique.backends.s3backend

import java.nio.file
import graphique.backends

private[s3backend] class S3Paths extends backends.Paths(file.Paths.get(""))
