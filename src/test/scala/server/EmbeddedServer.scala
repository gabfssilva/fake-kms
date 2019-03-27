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
    val host = "localhost"
    val socket = new ServerSocket(0)
    val port = socket.getLocalPort

    socket.close()
    val init = System.currentTimeMillis()
    val server = Await.result(Http().bindAndHandle(routes, host, port), 20 seconds)
    val end = System.currentTimeMillis()
    system.log.info(s"Server running at $host:$port and took ${end - init} ms to start")
    test(host, port)
    Await.result(server.terminate(20 seconds), 20 seconds)
    system.log.info(s"Server terminated")
  }
}
