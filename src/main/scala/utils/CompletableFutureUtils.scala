package utils

import java.util.concurrent.CompletableFuture

import scala.concurrent.{Future, Promise}

object CompletableFutureUtils {
  def toScala[T](c: CompletableFuture[T]): Future[T] = {
    val p = Promise[T]()
    c.whenComplete { (v, e) => if (e == null) p.success(v) else p.failure(e) }
    p.future
  }

  implicit class CompletableFutureImplicits[T](c: CompletableFuture[T]) {
    def toScala: Future[T] = CompletableFutureUtils.toScala(c)
  }
}