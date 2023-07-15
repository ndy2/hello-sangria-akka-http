package schema

import sangria.macros.LiteralGraphQLStringContext
import sangria.schema.*

trait RealworldSchemaDemo05 {
  val ast =
    graphql"""
      schema {
        query: Hello
      }

      type Hello {
        bar: Bar
      }

      type Bar {
        isColor: Boolean
      }
    """

  val schema: Schema[Any, Any] =  Schema.buildFromAst(ast)
}
// query { bar }
// -> Field 'bar' of type 'Bar' must have a sub selection. (line 2, column 3):\n  bar\n  ^"

