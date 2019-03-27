package base

import java.net.URI

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import client.KmsAsyncClientLocal
import endpoints.KmsEndpoint
import org.scalatest.{BeforeAndAfter, FeatureSpec, Matchers}
import security.FakeCryptographyHandler
import server.EmbeddedServer
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.services.kms.KmsAsyncClient
import utils.JsonSupport

trait BaseKmsFeature
  extends FeatureSpec
    with BeforeAndAfter
    with ScalatestRouteTest
    with Matchers
    with JsonSupport
    with EmbeddedServer {

  val cryptographyHandler = new FakeCryptographyHandler
  val kmsClient = new KmsAsyncClientLocal(cryptographyHandler)
  val kmsEndpoint = new KmsEndpoint(kmsClient)

  val routes: Route = Route.seal(kmsEndpoint.routes)

  def withKmsClient(host: String, port: Int)(f: KmsAsyncClient => Any): Any = {
    def kmsClient: KmsAsyncClient = KmsAsyncClient
      .builder()
      .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("x", "x")))
      .endpointOverride(URI.create(s"http://$host:$port"))
      .build()

    f(kmsClient)
  }
}
