package endpoints

import actions.KmsAction
import akka.http.scaladsl.server.{Directive1, Directives, Route}
import utils.JsonSupport

import scala.concurrent.ExecutionContext

trait Endpoint extends Directives with JsonSupport {
  implicit val ec: ExecutionContext = ExecutionContext.global

  def routes: Route
}

trait AwsEndpoint extends Endpoint {
  def kmsAction[Req, Resp](action: KmsAction[Req, Resp]): Directive1[Req] =
    post & headerValueByName("X-Amz-Target")
      .map(KmsAction.withNameInsensitive)
      .map(_.asInstanceOf[KmsAction[Req, Resp]])
      .filter(_ == action)
      .flatMap(a => entity(a.requestUnmarshaller))
}
