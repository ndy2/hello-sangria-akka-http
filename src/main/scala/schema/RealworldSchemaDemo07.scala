package schema

import io.circe.Json
import sangria.macros.LiteralGraphQLStringContext
import sangria.marshalling.circe.CirceInputUnmarshaller
import sangria.schema.*
import sangria.schema.AstSchemaBuilder.resolverBased

trait RealworldSchemaDemo07 {
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
  private val trueFn: Context[Any, _] => Action[Any, _] = (_: Context[Any, _]) => true
  private val falseFn: Context[Any, _] => Action[Any, _] = (_: Context[Any, _]) => false

  private val builder: ResolverBasedAstSchemaBuilder[Any] = resolverBased[Any](
    FieldResolver map "Query" -> Map(
      "bar" -> trueFn,
      "foo" -> falseFn
    ),
    AnyFieldResolver.defaultInput[Any, Json]
  )
    .validateSchemaWithException(ast)

  val schema: Schema[Any, Any] = Schema.buildFromAst(ast, builder)
}
