package graphique

/**
 * A processor of images.
 */
trait ImageProcessor {

  /**
   * Processes the input raw image to produce the output raw image.
   *
   * @param rawImage input image
   * @return output image
   */
  def process(rawImage: Array[Byte]): Array[Byte]
}
