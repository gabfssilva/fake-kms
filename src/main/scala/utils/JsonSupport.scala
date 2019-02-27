package utils

import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.Json.Null
import io.circe._
import io.circe.generic.extras.{AutoDerivation, Configuration => CirceConfig}
import io.circe.syntax._

import scala.language.implicitConversions

object JsonSupport extends JsonSupport

trait JsonSupport
  extends AutoDerivation
    with ErrorAccumulatingCirceSupport {

  implicit val printer: Printer = Printer.noSpaces.copy(dropNullValues = true)
  implicit val config: CirceConfig = CirceConfig.default.withSnakeCaseMemberNames


  implicit def valueListAsJson[A](value: List[A])(implicit encoder: Encoder[List[A]]): Json = value match {
    case Nil => Json.Null
    case list => encoder(list)
  }

  implicit def valueAsJson[A](value: A)(implicit encoder: Encoder[A]): Json = Encoder[A](encoder)(value)

  implicit def optionAsJson[A](op: Option[A])(implicit encoder: Encoder[A]): Json = op.map(valueAsJson(_)(encoder)).getOrElse(Null)

  implicit class AnyObjectToJsonImplicit[T](obj: T) {
    implicit def toJson(implicit e: Encoder[T]): Json = obj.asJson
  }

  implicit class StringJsonImplicits(j: String) {
    implicit def unsafeToObject[T](implicit d: Decoder[T]): T = parser.parse(j).flatMap(_.toObject[T]).getOrElse(throw new RuntimeException("parse error"))

    implicit def toObject[T](implicit d: Decoder[T]): Either[Exception, T] = parser.parse(j).flatMap(_.toObject[T])
  }

  implicit class CirceJsonImplicits(j: Json) {
    implicit def unsafeToObject[T](implicit d: Decoder[T]): T = j.toObject[T].getOrElse(throw new RuntimeException("parse error"))

    implicit def toObject[T](implicit d: Decoder[T]): Either[Exception, T] = j.as[T]
  }

}