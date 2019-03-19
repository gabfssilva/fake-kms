package domain

import scala.util.Try

case class Base64(string: String) {
  val maybeDecoded: Either[Throwable, Array[Byte]] = Try(java.util.Base64.getDecoder.decode(string)).toEither

  require(maybeDecoded.isRight, "string is not base64")

  def decoded: String = new String(maybeDecoded.getOrElse(null))
}
