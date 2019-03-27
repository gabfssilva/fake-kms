package domain

import scala.util.Try

import java.util.{Base64 => JBase64}

object Base64 {
  def apply(data: Array[Byte]): Base64 = new Base64(new String(data))

  def encode(string: String): Base64 = Base64(JBase64.getEncoder.encode(string.getBytes))
}

case class Base64(base64AsString: String) {
  val maybeDecoded: Either[Throwable, String] = Try(JBase64.getDecoder.decode(base64AsString)).toEither.map(a => new String(a))

  require(maybeDecoded.isRight, "string is not base64")

  def unsafeDecoded: String = maybeDecoded match {
    case Left(e) => throw e
    case Right(v) => v
  }
}
