package endpoints

import actions.KmsAction
import akka.http.scaladsl.server.Route
import json.JsonFormats
import software.amazon.awssdk.services.kms.KmsAsyncClient
import software.amazon.awssdk.services.kms.model.{CreateAliasRequest, DecryptRequest, EncryptRequest}
import utils.CompletableFutureUtils._

class KmsEndpoint(kmsClient: KmsAsyncClient)
  extends AwsEndpoint
    with JsonFormats {

  def encrypt: Route = kmsAction(KmsAction.Encrypt) { request: EncryptRequest =>
    complete(kmsClient.encrypt(request).toScala)
  }

  def decrypt: Route = kmsAction(KmsAction.Decrypt) { request: DecryptRequest =>
    complete(kmsClient.decrypt(request).toScala)
  }

  def createAlias: Route = kmsAction(KmsAction.CreateAlias) { request: CreateAliasRequest =>
    complete(kmsClient.createAlias(request).toScala)
  }

  override def routes: Route = encrypt ~ decrypt ~ createAlias
}
