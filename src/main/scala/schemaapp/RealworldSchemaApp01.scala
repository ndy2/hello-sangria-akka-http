package schemaapp

import sangria.macros.derive.deriveObjectType
import sangria.renderer.SchemaRenderer
import sangria.schema.*

trait RealworldSchemaApp01 {
  case class Bar(
      a: Boolean,
      b: List[String]
  )

  private val allBars =
    Bar(a = true, b = "haha" :: "papa" :: Nil) ::
      Bar(a = false, b = "hoho" :: "popo" :: Nil) ::
      Bar(a = false, b = "nana" :: "nini" :: Nil) :: Nil

  private val barType = deriveObjectType[Unit, Bar]()
  private val queryType = ObjectType[Unit, Unit](
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

  val schema = Schema(query = queryType)
  SchemaRenderer.renderSchema(schema)
}
