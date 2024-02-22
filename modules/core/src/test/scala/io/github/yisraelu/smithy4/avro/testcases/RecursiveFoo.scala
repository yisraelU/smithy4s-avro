package avro.testcases

import smithy4s.ShapeId
import smithy4s.schema.Schema
import smithy4s.schema.Schema._

case class RecursiveFoo(foo: Option[RecursiveFoo])

object RecursiveFoo {
  val schema: Schema[RecursiveFoo] =
    recursive {
      val foos = schema.optional[RecursiveFoo]("foo", _.foo)
      struct(foos)(RecursiveFoo.apply).withId(ShapeId("avro.testcases", "RecursiveFoo"))
    }.withId(ShapeId("avro.testcases", "RecursiveFoo"))
}
