package schemaapp

import sangria.macros.derive.deriveObjectType
import sangria.schema.*

trait RealworldSchemaApp02 {
  case class Bar(
      a: Boolean,
      b: List[String]
  )
  private val allBars =
    Bar(a = true, b = "haha" :: "papa" :: Nil) ::
      Bar(a = false, b = "hoho" :: "popo" :: Nil) ::
      Bar(a = false, b = "nana" :: "nini" :: Nil) :: Nil
  private val barType = deriveObjectType[Unit, Bar]()
  private val barQueryType = ObjectType[Unit, Unit](
    "Query",
    fields(
      Field(
        name = "allBars",
        fieldType = ListType(barType),
        description = Some("get all bars"),
        resolve = (_: Context[Unit, Unit]) => allBars
      )
    )
  )
  val barSchema = Schema(query = barQueryType)

  case class Foo(
      a: String,
      b: Double
  )
  private val allFoos = Foo("foo1", 0.0) :: Foo("foo2", 0.0) :: Nil
  private val fooType = deriveObjectType[Unit, Foo]()
  private val fooQueryType = ObjectType[Unit, Unit](
    "Query",
    fields(
      Field(
        name = "allFoos",
        fieldType = ListType(fooType),
        description = Some("get all foos"),
        resolve = (_: Context[Unit, Unit]) => allFoos
      )
    )
  )
  private val fooSchema = Schema(query = fooQueryType)

  val schema = barSchema.extend(fooSchema.toAst) // does not work
}
