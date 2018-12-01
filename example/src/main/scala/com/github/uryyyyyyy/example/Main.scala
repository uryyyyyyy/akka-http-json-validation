package com.github.uryyyyyyy.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Main extends CustomValidationDirectives with CustomJsonFormat {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()

  def setShutDownHook(binding: Future[Http.ServerBinding]) = {
    implicit val dispatcher = system.dispatcher
    scala.sys.addShutdownHook {
      println("Terminating...")
      binding
        .flatMap(_.unbind())
        .onComplete { _ =>
          materializer.shutdown()
          system.terminate()
        }
      Await.result(system.whenTerminated, 60.seconds)
      println("Terminated... Bye")
    }
  }

  val route = post {
    path("tag") {
      validate(as[Tag]) { validatedTag =>
        complete(validatedTag)
      }
    } ~ path("tags") {
      validate(as[Seq[Tag]]) { validatedTags =>
        complete(validatedTags)
      }
    } ~ path("nestedTag") {
      validate(as[NestedTag]) { nestedTag =>
        complete(nestedTag)
      }
    }
  }

  def main(args: Array[String]) {
    val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 9000)
    setShutDownHook(bindingFuture)
    println(s"Server started")
  }
}
