package schema

import sangria.schema.*

trait RealworldSchemaDemo4 {
  import sangria.marshalling.circe.CirceInputUnmarshaller
  val schema: Schema[Any, Any] = Schema.buildFromIntrospection(???)
}

