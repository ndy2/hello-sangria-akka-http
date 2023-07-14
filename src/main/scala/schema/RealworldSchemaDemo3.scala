package schema

import sangria.macros.LiteralGraphQLStringContext
import sangria.schema.*

trait RealworldSchemaDemo3 {
  private val ast = graphql"""
  type Query {
    hello: String!
    world: String!
  }
  """

  val schema: Schema[Any, Any] = Schema.buildFromAst(ast)
}

// query { world }
// -> Schema was materialized and cannot be used for any queries except introspection queries.
