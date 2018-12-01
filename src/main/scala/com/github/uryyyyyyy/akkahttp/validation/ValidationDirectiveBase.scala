package com.github.uryyyyyyy.akkahttp.validation

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directive1, Directives}
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import spray.json.{JsArray, JsField, JsObject, JsString, JsValue}

trait ValidatorBase[T] {
  def validate(model: T): Option[JsValue]
}

trait ValidatorSeq[T] extends ValidatorBase[Seq[T]] {
  type Validation = Seq[T] => Option[JsValue]

  protected def genValidation(rule: Seq[T] => Boolean, errorText: String): Validation = { models: Seq[T] =>
    if (rule(models)) Some(JsString(errorText)) else None
  }

  val validatorInternal: ValidatorBase[T]
  val validations: Seq[Validation]

  override def validate(models: Seq[T]): Option[JsValue] = {
    val opt = validations.map(v => v(models)).filter(_.isDefined)
    if (opt.nonEmpty) {
      Some(JsArray(opt.map(_.get):_*))
    } else {
      val values = models.zipWithIndex.map{ case(model, index) => (validatorInternal.validate(model), index)}.filter(_._1.isDefined)
      if (values.isEmpty) None else Some(JsObject(values.map{ case(js, i) => (i.toString, js.get)} :_*))
    }
  }
}

trait Validator[T] extends ValidatorBase[T] {
  type Validation = T => Option[JsField]

  protected def genValidation(fieldName: String, rule: T => Boolean, errorText: String): Validation = { model: T =>
    if (rule(model)) Some((fieldName, JsString(errorText))) else None
  }

  protected def genValidationInternal[In](fieldName: String, extractor: T => In, validatorInternal: ValidatorBase[In]): Validation = { model: T =>
    val opt = validatorInternal.validate(extractor(model))
    if (opt.isDefined) Some((fieldName, opt.get)) else None
  }

  val validations: Seq[Validation]

  override def validate(model: T): Option[JsValue] = {
    val opt = validations.map(v => v(model)).filter(_.isDefined)
    if (opt.isEmpty) {
      None
    } else {
      Some(JsObject(opt.map(_.get) :_*))
    }
  }
}

trait ValidationDirectiveBase extends Directives with SprayJsonSupport {

  def as[T](implicit um: FromRequestUnmarshaller[T], validator: ValidatorBase[T]): (FromRequestUnmarshaller[T], ValidatorBase[T]) = (um, validator)

  def validate[T](tpl: (FromRequestUnmarshaller[T], ValidatorBase[T])): Directive1[T] = {
    val (um, validator) = tpl
    for {
      model <- entity(um)
      validated <- validateRecursive(model, validator)
    } yield validated
  }

  private def validateRecursive[T](model: T, validator: ValidatorBase[T]): Directive1[T] = {
    val obj = validator.validate(model)
    if (obj.isEmpty) {
      provide(model)
    } else {
      complete(StatusCodes.BadRequest, JsObject(
        ("error", JsString("validation error.")),
        ("details", obj.get))
      )
    }
  }
}
