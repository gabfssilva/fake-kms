package endpoints

import actions.KmsAction
import akka.http.scaladsl.server.{Directive1, Route}
import json.JsonFormats
import software.amazon.awssdk.services.kms.KmsAsyncClient
import software.amazon.awssdk.services.kms.model.{DecryptRequest, EncryptRequest}
import utils.CompletableFutureUtils._

class KmsEndpoint(kmsClient: KmsAsyncClient)
  extends AwsEndpoint
    with JsonFormats {

  def encrypt: Route = xAmzTarget(KmsAction.Encrypt) { request: EncryptRequest =>
    complete(kmsClient.encrypt(request).toScala)
  }

  def decrypt: Route = xAmzTarget(KmsAction.Decrypt) { request: DecryptRequest =>
    complete(kmsClient.decrypt(request).toScala)
  }

  override def routes: Route = encrypt ~ decrypt
}
