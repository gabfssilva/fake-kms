package features

import base.BaseKmsFeature
import domain.Base64
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.kms.model.{DecryptRequest, EncryptRequest, EncryptResponse}

class KmsDecryptFeature extends BaseKmsFeature {
  feature("KMS decrypt") {
    scenario("successfully decrypt request") {
      withEmbeddedServer(routes) { (host, port) =>
        withKmsClient(host, port) { kmsClient =>
          val plainPayload = "123"
          val payload: String = Base64.encode(plainPayload).base64AsString

          val encrypted: EncryptResponse =
            kmsClient
              .encrypt {
                EncryptRequest
                  .builder()
                  .keyId("default")
                  .plaintext(SdkBytes.fromUtf8String(payload))
                  .build()
              }
              .join

          val decryptResult =
            kmsClient
              .decrypt {
                DecryptRequest
                  .builder()
                  .ciphertextBlob(encrypted.ciphertextBlob())
                  .build()
              }
              .join()

          decryptResult.plaintext().asUtf8String() shouldBe plainPayload
        }
      }
    }
  }
}
