package realworld.api

import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import sangria.macros.derive.deriveInputObjectType
import sangria.marshalling.circe.circeDecoderFromInput
import sangria.schema.*

object RealworldQuery {
  case class RegisterUserInput(
    username: String ,
    email :  String ,
    password: String ,
  )
  private val registerUserInput = deriveInputObjectType[RegisterUserInput]()
  private val registerUserInputArg = Argument("input", registerUserInput)
  private val registerUser = Field(
    name = "registerUser",
    fieldType = StringType,
    arguments = registerUserInputArg :: Nil,
    resolve = (_: Context[Unit, Unit]) => "hi"
  )

  private val mutationFields = fields(registerUser)
  val query = ObjectType[Unit, Unit]("Query", mutationFields)
}
