package client

import java.util.concurrent.CompletableFuture

import domain.Base64
import security.CryptographyHandler
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.kms.KmsAsyncClient
import software.amazon.awssdk.services.kms.model._

class KmsAsyncClientLocal(cryptographyHandler: CryptographyHandler) extends KmsAsyncClient {
  override def createAlias(createAliasRequest: CreateAliasRequest): CompletableFuture[CreateAliasResponse] = {
    def response: CreateAliasResponse = CreateAliasResponse
      .builder()
      .build()

    CompletableFuture.completedFuture(response)
  }

  override def encrypt(encryptRequest: EncryptRequest): CompletableFuture[EncryptResponse] = {
    def blob: SdkBytes = {
      def encodedValue: String = cryptographyHandler.encrypt(encryptRequest.plaintext().asUtf8String())
      SdkBytes.fromUtf8String(Base64.encode(encodedValue).base64AsString)
    }

    def response: EncryptResponse = EncryptResponse
      .builder()
      .keyId(encryptRequest.keyId())
      .ciphertextBlob(blob)
      .build()

    CompletableFuture.completedFuture(response)
  }

  override def decrypt(decryptRequest: DecryptRequest): CompletableFuture[DecryptResponse] = {
    def plain: SdkBytes = {
      def decrypted = cryptographyHandler.decrypt(Base64(decryptRequest.ciphertextBlob().asUtf8String()).unsafeDecoded)
      SdkBytes.fromUtf8String(decrypted)
    }

    def response: DecryptResponse = DecryptResponse
      .builder()
      .keyId("default")
      .plaintext(plain)
      .build()

    CompletableFuture.completedFuture(response)
  }

  override def serviceName(): String = "kms"
  override def close(): Unit = {}
}
