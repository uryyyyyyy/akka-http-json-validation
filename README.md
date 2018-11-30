akka-http-json-validation 
==========================

inspired by http://fruzenshtein.com/akka-http-another-one-validation-directive/

- 2.11 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.uryyyyyyy/akka-http-json-validation_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.uryyyyyyy/akka-http-json-validation_2.11)
- 2.12 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.uryyyyyyy/akka-http-json-validation_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.uryyyyyyy/akka-http-json-validation_2.12)

## Usage

see example

### setup

first, create class and validation.

```scala
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
```

then, create validation Directives

```scala
import com.github.uryyyyyyy.akkahttp.validation.ValidationDirectiveBase

trait CustomValidationDirectives extends ValidationDirectiveBase {
  implicit val tagV = TagValidator
}
```

then, extends its Directives & use directive method

```scala
object Main extends CustomValidationDirectives with CustomJsonFormat {

  val route = post {
    path("tag") {
      validateModel(asV[Tag]) { validatedTag =>
        complete(validatedTag)
      }
    }
  }
}
```

### verify

(you can run example)

#### normal object

```
# valid pattern
$ curl --silent -X POST -H "Content-Type: application/json" http://localhost:9000/tag -d '{"id": 1, "text": "tag1"}' | jq

# invalid pattern
$ curl --silent -X POST -H "Content-Type: application/json" http://localhost:9000/tag -d '{"id": -1, "text": ""}' | jq
{
  "error": "validation error.",
  "details": {
    "id": "id must be positive",
    "text": "text must not be empty"
  }
}
```

#### array object

```
# valid pattern
$ curl --silent -X POST -H "Content-Type: application/json" http://localhost:9000/tags -d '[{"id": 1, "text": "tag1"}]' | jq

# invalid pattern 1
$ curl --silent -X POST -H "Content-Type: application/json" http://localhost:9000/tags -d '[{"id": 1, "text": "tag1"}, {"id": -1, "text": ""}]' | jq
{
  "error": "validation error.",
  "details": {
    "1": {
      "id": "id must be positive",
      "text": "text must not be empty"
    }
  }
}

# invalid pattern 2
$ curl --silent -X POST -H "Content-Type: application/json" http://localhost:9000/tags -d '[]' | jq
{
  "error": "validation error.",
  "details": [
    "list must not be empty",
    "must not be empty2. (if you want, you can set more validation)"
  ]
}

# invalid pattern 3
$ curl --silent -X POST -H "Content-Type: application/json" http://localhost:9000/tags -d '[{"id": 1, "text": "tag1"}, {"id": 2, "text": "tag2"}, {"id": 3, "text": "tag3"}]' | jq
{
  "error": "validation error.",
  "details": [
    "list must be less than 3"
  ]
}
```

#### nested object

```
# valid pattern
$ curl --silent -X POST -H "Content-Type: application/json" http://localhost:9000/nestedTag -d '{"tag": {"id": 1, "text": "tag1"}, "tags": [{"id": 2, "text": "tag2"}]}' | jq

# invalid pattern
$ curl --silent -X POST -H "Content-Type: application/json" http://localhost:9000/nestedTag -d '{"tag": {"id": -1, "text": "tag1"}, "tags": []}' | jq
{
  "error": "validation error.",
  "details": {
    "tag": {
      "id": "id must be positive"
    },
    "tags": [
      "list must not be empty",
      "must not be empty2. (if you want, you can set more validation)"
    ]
  }
}
```
