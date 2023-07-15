package realworld

import realworld.api.{RealworldMutation, RealworldQuery}
import sangria.schema.Schema

trait RealworldSchema {

  val schema = Schema(
    query =  RealworldQuery.query,
    mutation = Some(RealworldMutation.mutation)
  )
}
