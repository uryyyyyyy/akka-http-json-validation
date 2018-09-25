package com.github.uryyyyyyy.example

import com.github.uryyyyyyy.akkahttp.validation.ValidationDirectiveBase

trait CustomValidationDirectives extends ValidationDirectiveBase {
  implicit val tagv = TagValidator
  implicit val taglistv = TagListValidator
}
