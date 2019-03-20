package actions

import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import enumeratum._
import software.amazon.awssdk.services.kms.model._

import scala.collection.immutable
import json.JsonFormats._

sealed abstract class KmsAction[Req, Resp](override val entryName: String)(implicit val requestUnmarshaller: FromRequestUnmarshaller[Req]) extends EnumEntry

object KmsAction extends Enum[KmsAction[_, _]] {
  override val values: immutable.IndexedSeq[KmsAction[_, _]] = findValues

  case object Encrypt extends KmsAction[EncryptRequest, EncryptResponse]("TrentService.Encrypt")
  case object Decrypt extends KmsAction[DecryptRequest, DecryptResponse]("TrentService.Decrypt")
  case object CreateAlias extends KmsAction[CreateAliasRequest, CreateAliasResponse]("TrentService.CreateAlias")
}
