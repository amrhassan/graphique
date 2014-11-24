package graphique.backends

/**
 * Thrown when [[Backend.submit()]] fails.
 */
case class ImageSubmissionError(imageTag: String, cause: Throwable) extends
  RuntimeException(s"Failed to submit $imageTag", cause)
