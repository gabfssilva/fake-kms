package endpoints

import actions.KmsAction
import akka.http.scaladsl.server.Route
import json.JsonFormats
import software.amazon.awssdk.services.kms.KmsAsyncClient

class KmsEndpoint(kmsClient: KmsAsyncClient)
  extends AwsEndpoint
    with JsonFormats {

  def encrypt: Route = handleKmsAction(KmsAction.Encrypt)(r => kmsClient.encrypt(r))
  def decrypt: Route = handleKmsAction(KmsAction.Decrypt)(r => kmsClient.decrypt(r))
  def createAlias: Route = handleKmsAction(KmsAction.CreateAlias)(r => kmsClient.createAlias(r))

  override def routes: Route = encrypt ~ decrypt ~ createAlias
}
