package endpoints

import actions.KmsAction
import akka.http.scaladsl.server.{Directive1, Directives, Route}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import utils.JsonSupport

import scala.concurrent.ExecutionContext

trait Endpoint extends Directives with JsonSupport with ErrorAccumulatingCirceSupport {
  implicit val ec: ExecutionContext = ExecutionContext.global

  def routes: Route
}

trait AwsEndpoint extends Endpoint {
  def xAmzTarget[Req, Resp](action: KmsAction[Req, Resp]): Directive1[Req] =
    headerValueByName("X-Amz-Target")
      .map(KmsAction.withNameInsensitive)
      .map(_.asInstanceOf[KmsAction[Req, Resp]])
      .filter(_ == action)
      .flatMap(a => entity(a.requestUnmarshaller))
}
