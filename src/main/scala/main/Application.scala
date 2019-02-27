package main

import akka.http.scaladsl.server.{HttpApp, Route}
import client.KmsAsyncClientLocal
import endpoints.KmsEndpoint

object Application extends HttpApp with App {
  def kmsClient = new KmsAsyncClientLocal
  def kmsEndpoint = new KmsEndpoint(kmsClient)

  override protected def routes: Route = kmsEndpoint.routes

  startServer("0.0.0.0", 9000)
}