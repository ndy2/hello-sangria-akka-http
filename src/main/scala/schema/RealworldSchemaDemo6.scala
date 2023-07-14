package schema

import io.circe.Json
import sangria.macros.LiteralGraphQLStringContext
import sangria.marshalling.circe.CirceInputUnmarshaller
import sangria.schema.*
import sangria.schema.AstSchemaBuilder.resolverBased

trait RealworldSchemaDemo6 {
  val ast =
    graphql"""
      schema {
        query: Query
      }

      type Query {
        bar: Boolean
        foo: Boolean
      }
    """

  val builder = resolverBased[Any](
    FieldResolver.map(
      "Query" -> Map(
        "bar" -> (_ => true),
        "foo" -> (_ => false)
      )
    ),
    AnyFieldResolver.defaultInput[Any, Json]
  )
    .validateSchemaWithException(ast)

  val schema: Schema[Any, Any] = Schema.buildFromAst(ast, builder)
}

/** req
 query {
  bar
  foo
}
 */

/** resp
{
  "data": {
    "bar": true,
    "foo": false
}
 */
