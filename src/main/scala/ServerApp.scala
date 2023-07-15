import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes.*
import akka.http.scaladsl.server.*
import akka.http.scaladsl.server.Directives.*
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.http.akka.circe.CirceHttpSupport
import sangria.marshalling.circe.*
import schemaapp.RealworldSchemaApp03

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object ServerApp
  extends App
    with CorsSupport
    with CirceHttpSupport
    with RealworldSchemaApp03 {
  implicit val system: ActorSystem = ActorSystem("sangria-server")

  val route: Route =
    optionalHeaderValueByName("X-Apollo-Tracing") { _ =>
      path("graphql") {
        graphQLPlayground ~
          prepareGraphQLRequest {
            case Success(req) =>
              val graphQLResponse = Executor
                .execute(
                  schema = schema,
                  queryAst = req.query,
                  userContext = (),
                  variables = req.variables,
                  operationName = req.operationName
                )
                .map(OK -> _)
                .recover {
                  case error: QueryAnalysisError =>
                    BadRequest -> error.resolveError
                  case error: ErrorWithResolver =>
                    InternalServerError -> error.resolveError
                }
              complete(graphQLResponse)
            case Failure(preparationError) =>
              complete(BadRequest, formatError(preparationError))
          }
      }
    } ~
      (get & pathEndOrSingleSlash) {
        redirect("/graphql", PermanentRedirect)
      }

  val PORT = sys.props.get("http.port").fold(8080)(_.toInt)
  val INTERFACE = "0.0.0.0"
  Http().newServerAt(INTERFACE, PORT).bindFlow(corsHandler(route))
}
