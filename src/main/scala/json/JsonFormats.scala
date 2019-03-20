package json

import domain.Base64
import io.circe.{Decoder, Encoder, HCursor, Json}
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.kms.model._
import utils.JsonSupport

object JsonFormats extends JsonFormats

trait JsonFormats
  extends EncryptJsonFormat
    with DecryptJsonFormat
    with CreateAliasJsonFormat

trait EncryptJsonFormat extends JsonSupport {
  implicit val encryptRequestDecoder: Decoder[EncryptRequest] = (c: HCursor) => for {
    keyId <- c.downField("KeyId").as[String]
    plainText <- c.downField("Plaintext").as[String]
  } yield EncryptRequest.builder().keyId(keyId).plaintext(SdkBytes.fromUtf8String(Base64(plainText).string)).build()

  implicit val encryptResponseEncoder: Encoder[EncryptResponse] = (response: EncryptResponse) => {
    Json.obj(
      "CiphertextBlob" -> response.ciphertextBlob().asUtf8String(),
      "KeyId" -> response.keyId()
    )
  }
}

trait DecryptJsonFormat extends JsonSupport {
  implicit val decryptRequestDecoder: Decoder[DecryptRequest] = (c: HCursor) => for {
    blob <- c.downField("CiphertextBlob").as[String]
  } yield DecryptRequest.builder().ciphertextBlob(SdkBytes.fromUtf8String(Base64(blob).string)).build()

  implicit val decryptResponseEncoder: Encoder[DecryptResponse] = (response: DecryptResponse) => {
    Json.obj(
      "KeyId" -> response.keyId(),
      "Plaintext" -> response.plaintext().asUtf8String()
    )
  }
}

trait CreateAliasJsonFormat extends JsonSupport {
  implicit val createAliasRequestDecoder: Decoder[CreateAliasRequest] = (c: HCursor) => for {
    aliasName <- c.downField("AliasName").as[String]
  } yield CreateAliasRequest.builder().aliasName(aliasName).build()

  implicit val createAliasResponseEncoder: Encoder[CreateAliasResponse] = (response: CreateAliasResponse) => {
    Json.Null
  }
}