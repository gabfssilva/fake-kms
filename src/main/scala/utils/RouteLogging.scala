package utils

import java.lang.System.currentTimeMillis

import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteResult.Complete
import akka.http.scaladsl.server.directives.{DebuggingDirectives, LogEntry, LoggingMagnet}

object RouteLogging {
  val loggableHeaders = List("connection", "x-amz-target", "content-type", "accept")

  def logRequestAndResponse(loggingAdapter: LoggingAdapter, before: Long)(req: HttpRequest)(res: Any): Unit = {
    val entry = res match {
      case Complete(resp) =>
        val message = s"""path="${req.uri}" ${req.headers.filter(h => loggableHeaders.contains(h.lowercaseName())).map(h => s"""${h.lowercaseName()}="${h.value()}"""").mkString(" ")} method="${req.method.value}" status=${resp.status.intValue()} elapsedTimeInMs=${currentTimeMillis() - before} """
        LogEntry(message, Logging.InfoLevel)
      case other => LogEntry(other, Logging.InfoLevel)
    }

    entry.logTo(loggingAdapter)
  }

  def loggableRoute(route: Route): Route = {
    DebuggingDirectives.logRequestResult(LoggingMagnet(log => {
      val requestTimestamp = currentTimeMillis()
      logRequestAndResponse(log, requestTimestamp)
    }))(route)
  }
}
