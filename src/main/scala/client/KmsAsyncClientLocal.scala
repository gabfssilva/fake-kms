package client

import java.util.concurrent.CompletableFuture

import software.amazon.awssdk.services.kms.KmsAsyncClient
import software.amazon.awssdk.services.kms.model._

class KmsAsyncClientLocal extends KmsAsyncClient {
  override def createAlias(createAliasRequest: CreateAliasRequest): CompletableFuture[CreateAliasResponse] = {
    def response: CreateAliasResponse = CreateAliasResponse
      .builder()
      .build()

    CompletableFuture.completedFuture(response)
  }

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
      .plaintext(decryptRequest.ciphertextBlob())
      .build()

    CompletableFuture.completedFuture(response)
  }

  override def serviceName(): String = "kms"
  override def close(): Unit = {}
}
