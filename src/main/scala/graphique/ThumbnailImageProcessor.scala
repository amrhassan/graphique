package graphique

/**
 * A processor of raw images into thumbnails.
 *
 * @param outputDimension output dimensions of thumbnails
 */
class ThumbnailImageProcessor(outputDimension: Dimensions) extends ImageProcessor {
  def process(rawImage: Array[Byte]): Array[Byte] = ???
}
