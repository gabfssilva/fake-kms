package base

import java.net.URI

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import client.KmsAsyncClientLocal
import endpoints.KmsEndpoint
import org.scalatest.{BeforeAndAfter, FeatureSpec, Matchers}
import security.{CryptographyHandler, FakeCryptographyHandler}
import server.EmbeddedServer
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.kms.KmsAsyncClient
import utils.JsonSupport

object BaseKmsFeature {
  val cryptographyHandler = new FakeCryptographyHandler
  val kmsClient = new KmsAsyncClientLocal(cryptographyHandler)
  val kmsEndpoint = new KmsEndpoint(kmsClient)

  val routes: Route = kmsEndpoint.auditedRoutes
}

trait BaseKmsFeature
  extends FeatureSpec
    with BeforeAndAfter
    with ScalatestRouteTest
    with Matchers
    with JsonSupport
    with EmbeddedServer {

  val cryptographyHandler: CryptographyHandler = BaseKmsFeature.cryptographyHandler
  val routes: Route = BaseKmsFeature.routes

  def withKmsClient(host: String, port: Int)(f: KmsAsyncClient => Any): Any = {
    def kmsClient: KmsAsyncClient = KmsAsyncClient
      .builder()
      .region(Region.US_EAST_1)
      .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("x", "x")))
      .endpointOverride(URI.create(s"http://$host:$port"))
      .build()

    f(kmsClient)
  }
}
