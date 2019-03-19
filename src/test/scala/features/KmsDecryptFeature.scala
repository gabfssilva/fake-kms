package features

import base.BaseKmsFeature
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.kms.model.{DecryptRequest, EncryptRequest, EncryptResponse}

class KmsDecryptFeature extends BaseKmsFeature {
  feature("KMS decrypt") {
    scenario("successfully decrypt request") {
      withEmbeddedServer(routes) { (host, port) =>
        withKmsClient(host, port) { kmsClient =>
          val payload = "123"

          val result: EncryptResponse =
            kmsClient
              .encrypt {
                EncryptRequest
                  .builder()
                  .keyId("default")
                  .plaintext(SdkBytes.fromUtf8String(payload))
                  .build()
              }
              .join

          val decryptResult = kmsClient
            .decrypt {
              DecryptRequest
                .builder()
                .ciphertextBlob(result.ciphertextBlob())
                .build()
            }
            .join()

          decryptResult.plaintext().asUtf8String() shouldBe payload
        }
      }
    }
  }
}
