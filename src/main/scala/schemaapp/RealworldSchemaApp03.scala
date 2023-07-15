package schemaapp

import sangria.macros.derive.deriveObjectType
import sangria.schema.*

trait RealworldSchemaApp03 {
  case class Bar(
                  a: Boolean,
                  b: List[String]
                )
  private val allBarsField = Field(
    name = "allBars",
    fieldType = ListType(deriveObjectType[Unit, Bar]()),
    description = Some("get all bars"),
    resolve = (_: Context[Unit, Unit]) => Bar(a = true, b = "haha" :: "papa" :: Nil) ::
      Bar(a = false, b = "hoho" :: "popo" :: Nil) ::
      Bar(a = false, b = "nana" :: "nini" :: Nil) :: Nil
  )

  case class Foo(
                  a: String,
                  b: Double
                )
  private val allFoosField = Field(
    name = "allFoos",
    fieldType = ListType(deriveObjectType[Unit, Foo]()),
    description = Some("get all foos"),
    resolve = (_: Context[Unit, Unit]) => Foo("foo1", 0.0) :: Foo("foo2", 0.5) :: Nil
  )

  private val queryType = ObjectType[Unit, Unit](
    "Query",
    fields(
      allFoosField,
      allBarsField
    )
  )

  val schema = Schema(queryType)
}
