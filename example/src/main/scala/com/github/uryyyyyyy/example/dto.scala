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
      tag => tag.id <= 0,
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

  private def emptyRule2: Validation =
    genValidation(
      req => req.isEmpty,
      "must not be empty2. (if you want, you can set more validation)"
    )

  private def lessThan3Rule: Validation =
    genValidation(
      req => req.length >= 3,
      "list must be less than 3"
    )

  val validations = Seq(emptyRule, emptyRule2, lessThan3Rule)
}

case class NestedTag(
  tag: Tag,
  tags: Seq[Tag]
)

object NestedTagValidator extends Validator[NestedTag] {

  private def tagRule: Validation =
    genValidationInternal(
      "tag",
      nested => nested.tag,
      TagValidator
    )

  private def tagsRule: Validation =
    genValidationInternal(
      "tags",
      nested => nested.tags,
      TagListValidator
    )

  val validations = Seq(tagRule, tagsRule)
}