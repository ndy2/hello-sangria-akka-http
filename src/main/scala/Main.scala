import sangria.execution.Executor
import sangria.macros.LiteralGraphQLStringContext
import sangria.schema.AstSchemaBuilder.resolverBased
import sangria.schema.{Action, Context, FieldResolver, Schema}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object Main extends App {


  private val document =
    graphql"""
        type Query {
            hello(name: String) : String
         }
      """

  def createdResolver(ctx: Context[Any, _]): Action[Any, _] = {
    val name = ctx.args.arg[String]("name")
    Resolver.hello(name)
  }

  private val builder = resolverBased(
    FieldResolver map (
      "Query" -> Map("hello" -> createdResolver _))
  )


  private val schema = Schema.buildFromAst(document, builder)

  private val result = Executor.execute(
    schema,
    graphql"""
             query {
              hello(name: "sangria")
            }
           """
  )
  Await.result(result, 5 second)
  println(result)
}

object Resolver {
  def hello(name: String): String = {
    "world"
  }
}
