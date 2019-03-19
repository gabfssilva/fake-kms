package main

import akka.http.scaladsl.server.{HttpApp, Route}
import client.KmsAsyncClientLocal
import endpoints.KmsEndpoint

object Application extends HttpApp with App {
  def hostname: String = Option(System.getenv("APP_HOSTNAME")).getOrElse("0.0.0.0")
  def port: Int = Option(System.getenv("APP_PORT")).map(_.toInt).getOrElse(9000)

  def kmsClient = new KmsAsyncClientLocal
  def kmsEndpoint = new KmsEndpoint(kmsClient)

  override protected def routes: Route = kmsEndpoint.routes

  startServer(hostname, port)
}