package client

import java.util.Base64
import java.util.concurrent.CompletableFuture

import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.kms.KmsAsyncClient
import software.amazon.awssdk.services.kms.model.{DecryptRequest, DecryptResponse, EncryptRequest, EncryptResponse}

class KmsAsyncClientLocal extends KmsAsyncClient {
  override def encrypt(encryptRequest: EncryptRequest): CompletableFuture[EncryptResponse] = {
    def response: EncryptResponse = EncryptResponse
        .builder()
        .keyId(encryptRequest.keyId())
        .ciphertextBlob(encryptRequest.plaintext())
        .build()

    CompletableFuture.completedFuture(response)
  }

  override def decrypt(decryptRequest: DecryptRequest): CompletableFuture[DecryptResponse] = {
    def response: DecryptResponse = DecryptResponse
      .builder()
      .keyId("default")
      .plaintext(SdkBytes.fromByteArray(Base64.getDecoder.decode(decryptRequest.ciphertextBlob().asByteArray())))
      .build()

    CompletableFuture.completedFuture(response)
  }

  override def serviceName(): String = "kms"
  override def close(): Unit = {}
}
