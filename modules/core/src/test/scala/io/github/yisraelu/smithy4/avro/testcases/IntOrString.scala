package avro.testcases

import smithy4s.ShapeId
import smithy4s.schema.Schema
import smithy4s.schema.Schema._

sealed trait IntOrString

object IntOrString {
  case class IntValue(value: Int) extends IntOrString

  case class StringValue(value: String) extends IntOrString

  val schema: Schema[IntOrString] = {
    val intValue = int.oneOf[IntOrString]("intValue", IntValue(_)) {
      case IntValue(int) => int
    }
    val stringValue = string.oneOf[IntOrString]("stringValue", StringValue(_)) {
      case StringValue(str) => str
    }
    union(intValue, stringValue).reflective.withId(ShapeId("avro.testcases", "IntOrString"))
  }
}
