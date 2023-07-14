package schema

import sangria.schema.*

trait RealworldSchemaDemo2  {
  private val QueryType = ObjectType("Query", fields[Unit, Unit](
    Field("hello", StringType, resolve = _ => "Hello world! 1"),
    Field("world", StringType, resolve = _ => "world world! 2"),
  ))

  val schema: Schema[Unit, Unit] = Schema(QueryType)
}
