package server

import java.net.ServerSocket

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.Await
import scala.concurrent.duration._

trait EmbeddedServer {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer

  def withEmbeddedServer(routes: Route)(test: (String, Int) => Any): Any = {
    var retries = 0
    var shouldRetry = false

    while(shouldRetry && retries < 20) {
      val host = "localhost"
      val socket = new ServerSocket(0)
      val port = socket.getLocalPort

      val server = Await.result(Http().bindAndHandle(routes, host, port), 20 seconds)

      try {
        test(host, port)
      } catch { case _: Throwable =>
        shouldRetry = true
      } finally {
        retries = retries + 1

        Await.result(server.terminate(20 seconds), 20 seconds)

        socket.close()
        system.log.info(s"Server terminated")
      }
    }
  }
}
