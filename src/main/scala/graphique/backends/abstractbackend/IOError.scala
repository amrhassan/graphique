package graphique.backends.abstractbackend

case class IOError(message: String, cause: Throwable) extends RuntimeException(message, cause)
