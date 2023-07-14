import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes.*
import akka.http.scaladsl.server.*
import akka.http.scaladsl.server.Directives.*
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.http.akka.circe.CirceHttpSupport
import sangria.marshalling.circe.*
import sangria.slowlog.SlowLog
import schema.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Server
    extends App
    with CorsSupport
    with CirceHttpSupport
    with RealworldSchemaDemo6 {
  implicit val system: ActorSystem = ActorSystem("sangria-server")

  val route: Route =
    optionalHeaderValueByName("X-Apollo-Tracing") { tracing =>
      path("graphql") {
        graphQLPlayground ~
          prepareGraphQLRequest {
            case Success(req) =>
              val middleware =
                if (tracing.isDefined) SlowLog.apolloTracing :: Nil else Nil
              val graphQLResponse = Executor
                .execute(
                  schema = schema,
                  queryAst = req.query,
                  userContext = (),
                  variables = req.variables,
                  operationName = req.operationName,
                  middleware = middleware
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
