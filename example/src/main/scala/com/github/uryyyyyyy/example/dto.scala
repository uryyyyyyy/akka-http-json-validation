package com.github.uryyyyyyy.example

import com.github.uryyyyyyy.akkahttp.validation.{Validator, ValidatorSeq}

case class Tag(
  id: Int,
  text: String
)

object TagValidator extends Validator[Tag] {

  private def idRule: Validation =
    genValidation(
      "id",
      tag => tag.id < 0,
      "id must be positive"
    )

  private def textRule: Validation =
    genValidation(
      "text",
      tag => tag.text.isEmpty,
      "text must not be empty"
    )

  val validations = Seq(idRule, textRule)
}

object TagListValidator extends ValidatorSeq[Tag] {

  val validatorInternal = TagValidator

  private def emptyRule: Validation =
    genValidation(
      req => req.isEmpty,
      "list must not be empty"
    )
  val validations = Seq(emptyRule)
}