package graphique.http

import graphique.images.{JPEGFormat, PNGFormat, ImageFormat, Dimensions}
import spray.httpx.unmarshalling._

object ImageAttributeDeserializers {

  /**
   * A deserializer for the Option[Dimensions] type.
   */
  val dimensionsOptionDeserializer = new FromStringOptionDeserializer[Option[Dimensions]] {
    val Pattern = """(\d+)[xX](\d+)""".r
    def apply(optionString: Option[String]) = optionString map {
      case Pattern(width, height) => Right(Some(Dimensions(width.toInt, height.toInt)))
      case _ => Left(new MalformedContent("Dimensions must be formatted like WIDTHxHeight"))
    } getOrElse Right(None)
  }

  /**
   * A deserializer for the Option[ImageFormat] type.
   */
  val imageFormatOptionDeserializer = new FromStringOptionDeserializer[Option[ImageFormat]] {
    val JPEGPattern = """(?i)JPEG\(([0-9.]+)\)""".r
    def apply(optionString: Option[String]) = optionString map {
      case "PNG" => Right(Some(PNGFormat))
      case "JPEG" => Right(Some(JPEGFormat()))
      case JPEGPattern(quality) if quality.toFloat <= 1 => Right(Some(JPEGFormat(quality.toFloat)))
      case _ => Left(
        new MalformedContent("The image format must be one of [JPEG, JPEG(quality), PNG] where (0 <= quality <= 1)"))
    } getOrElse Right(None)
  }
}