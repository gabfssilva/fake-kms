package endpoints

import actions.KmsAction
import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server._
import io.circe.Json
import utils.{CompletableFutureSupport, JsonSupport}

import scala.concurrent.{ExecutionContext, Future}

trait Endpoint extends Directives with JsonSupport {
  implicit val ec: ExecutionContext = ExecutionContext.global

  def routes: Route
}

trait XAmzTargetSupport extends Endpoint {
  private val headerName = "X-Amz-Target"

  case class XAmzTargetNotImplemented(target: String) extends Rejection

  val xAmzTargetNotImplementedHandler: RejectionHandler = RejectionHandler
    .newBuilder()
    .handle { case XAmzTargetNotImplemented(target) => complete(StatusCodes.BadRequest -> Json.obj("message" -> s"unknown $headerName $target")) }
    .result()

  def extractXAmzTarget[Req, Resp](action: KmsAction[Req, Resp]): Directive1[KmsAction[Req, Resp]] = {
    def inner: Directive1[KmsAction[Req, Resp]] = headerValueByName(headerName)
      .map(t => KmsAction.withNameInsensitiveOption(t) -> t)
      .tflatMap {
        case (Some(v), _) if v == action => provide(action)
        case (Some(_), _)                => reject()
        case (_, t)                      => reject(XAmzTargetNotImplemented(t))
      }

    handleRejections(xAmzTargetNotImplementedHandler) & inner
  }
}

trait AwsEndpoint extends Endpoint with XAmzTargetSupport with CompletableFutureSupport {
  def kmsAction[Req, Resp](action: KmsAction[Req, Resp]): Directive1[Req] =  post & extractXAmzTarget(action).flatMap(a => entity(a.requestUnmarshaller))

  def handleKmsAction[Req, Resp](action: KmsAction[Req, Resp])(f: Req => Future[Resp]): Route = {
    kmsAction(action) { req =>
      implicit val m: ToResponseMarshaller[Resp] = action.toResponseMarshaller
      complete(f(req))
    }
  }
}
