package schema

import sangria.macros.LiteralGraphQLStringContext
import sangria.schema.*
import sangria.schema.AstSchemaBuilder.resolverBased

trait RealworldSchemaDemo09 {
  val ast =
    graphql"""
      schema {
        query: Query
      }

      type Query {
        bar: [Boolean!]!
        foo: Boolean
      }
    """

  private val builder: ResolverBasedAstSchemaBuilder[Any] = resolverBased[Any](
    FieldResolver map "Query" -> Map(
      "bar" -> (_ => List(true, false, false, false)),
      "foo" -> (_ => false)
    )
  )
    .validateSchemaWithException(ast)

  val schema: Schema[Any, Any] = Schema.buildFromAst(ast, builder)
}
