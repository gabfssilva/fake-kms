package json

import io.circe.{Decoder, Encoder, HCursor, Json}
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.kms.model.{DecryptRequest, DecryptResponse, EncryptRequest, EncryptResponse}
import utils.JsonSupport

object JsonFormats extends JsonFormats

trait JsonFormats
  extends EncryptJsonFormat
    with DecryptJsonFormat

trait EncryptJsonFormat extends JsonSupport {
  implicit val encryptRequestDecoder: Decoder[EncryptRequest] = (c: HCursor) => for {
    keyId <- c.downField("KeyId").as[String]
    plainText <- c.downField("Plaintext").as[String]
  } yield EncryptRequest.builder().keyId(keyId).plaintext(SdkBytes.fromUtf8String(plainText)).build()

  implicit val encryptResponseEncoder: Encoder[EncryptResponse] = (response: EncryptResponse) => {
    Json.obj(
      "CiphertextBlob" -> response.ciphertextBlob().asUtf8String(),
      "KeyId" -> response.keyId()
    )
  }
}

trait DecryptJsonFormat extends JsonSupport {
  implicit val decryptRequestDecoder: Decoder[DecryptRequest] = (c: HCursor) => for {
    keyId <- c.downField("CiphertextBlob").as[String]
  } yield DecryptRequest.builder().ciphertextBlob(SdkBytes.fromUtf8String(keyId)).build()

  implicit val decryptResponseEncoder: Encoder[DecryptResponse] = (response: DecryptResponse) => {
    Json.obj(
      "KeyId" -> response.keyId(),
      "Plaintext" -> response.plaintext().asUtf8String()
    )
  }
}