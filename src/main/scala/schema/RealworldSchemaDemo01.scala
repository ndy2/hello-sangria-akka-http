package schema

import sangria.schema.*

trait RealworldSchemaDemo01 {
  private val QueryType = ObjectType("Query", fields[Unit, Unit](
    Field("hello", StringType, resolve = _ => "Hello world! 1")
  ))

  val schema: Schema[Unit, Unit] = Schema(QueryType)
}
