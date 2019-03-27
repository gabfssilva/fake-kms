package utils

import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.model.{HttpCharsets, MediaType}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import domain.Base64
import io.circe.Decoder.Result
import io.circe.Json.Null
import io.circe._

import io.circe.syntax._

import scala.collection.immutable.Seq
import scala.language.implicitConversions
import scala.util.{Failure, Success, Try}

object JsonSupport extends JsonSupport

trait JsonSupport extends ErrorAccumulatingCirceSupport {
  override def mediaTypes: Seq[MediaType.WithFixedCharset] =
    List(`application/json`, MediaType.applicationWithFixedCharset("x-amz-json-1.1", HttpCharsets.`UTF-8`))

  implicit def valueListAsJson[A](value: List[A])(implicit encoder: Encoder[List[A]]): Json = value match {
    case Nil => Json.Null
    case list => encoder(list)
  }

  implicit def valueAsJson[A](value: A)(implicit encoder: Encoder[A]): Json = Encoder[A](encoder)(value)
  implicit def optionAsJson[A](op: Option[A])(implicit encoder: Encoder[A]): Json = op.map(valueAsJson(_)(encoder)).getOrElse(Null)

  implicit val base64Decoder: Decoder[Base64] = (c: HCursor) => c.as[String].flatMap { v =>
    Try(Base64(v)) match {
      case Failure(_) =>
        Left(DecodingFailure("value is not Base64", c.history))
      case Success(value) =>
        Right(value)
    }
  }

  implicit val base64Encoder: Encoder[Base64] = (a: Base64) => a.base64AsString
}