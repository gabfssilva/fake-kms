package features

import base.BaseKmsFeature
import domain.Base64
import security.Security.Encrypted
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.kms.model.{EncryptRequest, EncryptResponse}

class KmsEncryptFeature extends BaseKmsFeature {
  feature("KMS encrypt") {
    scenario("successfully request encrypt request") {
      withEmbeddedServer(routes) { (host, port) =>
        withKmsClient(host, port) { kmsClient =>
          val payload: String = Base64.encode("123").base64AsString
          val encryptedPayload: Encrypted = cryptographyHandler.encrypt(payload)

          val result: EncryptResponse =
            kmsClient
              .encrypt(
                EncryptRequest
                  .builder()
                  .keyId("default")
                  .plaintext(SdkBytes.fromUtf8String(payload))
                  .build()
              )
              .join

          result.ciphertextBlob().asUtf8String() shouldBe encryptedPayload
        }
      }
    }
  }
}
