package graphique.backends

/**
 * Thrown when [[Backend.submit()]] fails.
 */
case class ImageSubmissionError(message: String, cause: Throwable) extends RuntimeException(message, cause)
