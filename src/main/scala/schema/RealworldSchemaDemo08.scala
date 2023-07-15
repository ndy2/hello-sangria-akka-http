package schema

import io.circe.Json
import sangria.macros.LiteralGraphQLStringContext
import sangria.marshalling.circe.CirceInputUnmarshaller
import sangria.schema.*
import sangria.schema.AstSchemaBuilder.resolverBased

trait RealworldSchemaDemo08 {
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

  private val trueAction: Action[Any, _ ] = Value(true)   // 그냥 true 도 가능
  private val falseAction: Action[Any, _ ] = Value(false) // 그냥 false 도 가능

  private val trueFn: Context[Any, _] => Action[Any, _] = _ => trueAction
  private val falseFn: Context[Any, _] => Action[Any, _] = _ => falseAction

  private val builder: ResolverBasedAstSchemaBuilder[Any] = resolverBased[Any](
    FieldResolver map "Query" -> Map(
      "bar" -> trueFn,
      "foo" -> falseFn
    ),
    AnyFieldResolver.defaultInput[Any, Json]
  )
  val schema: Schema[Any, Any] = Schema.buildFromAst(ast).extend(ast, builder)
}
