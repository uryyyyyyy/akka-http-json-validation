package com.github.uryyyyyyy.example

import com.github.uryyyyyyy.akkahttp.validation.ValidationDirectiveBase

trait CustomValidationDirectives extends ValidationDirectiveBase {
  implicit val tagV = TagValidator
  implicit val taglistV = TagListValidator
  implicit val nestedTagV = NestedTagValidator
}
