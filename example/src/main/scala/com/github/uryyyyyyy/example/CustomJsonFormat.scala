package com.github.uryyyyyyy.example

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, PrettyPrinter}

trait CustomJsonFormat extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val printer = PrettyPrinter
  implicit val tagFormat = jsonFormat2(Tag)
}