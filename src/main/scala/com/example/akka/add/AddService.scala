package com.example.akka.add

import scala.concurrent.{ExecutionContext, Future}
import com.example.akka.DefaultJsonFormats
import AddActor._
import akka.actor.ActorRef
import akka.util.Timeout
import akka.http.scaladsl.model.Uri.Path.Segment
import akka.http.scaladsl.server.Directives
import io.swagger.annotations._
import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes


// @Api           :
// @Path          :
//
// @ApiOperatioin :
//     - value       :
//     - notes
//     - nickname
//     - httpMethod
//     
// @ApiResponses  :
//      - ApiResponse
//            - code
//            - message
//            - response
// @ApiImplicitParams
//      - ApiImplicitParam :
//            - new
//            - value
//            - required
//            - dataType
//            - paramType
//
@Api(value = "/add", description = "Add Numbers", produces = "application/json")
@Path("/add")
class AddService(addActor: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

  import akka.pattern.ask
  import scala.concurrent.duration._

  implicit val timeout = Timeout(2.seconds)

  import spray.json.DefaultJsonProtocol._
  implicit val requestFormat = jsonFormat1(AddRequest)
  implicit val responseFormat = jsonFormat1(AddResponse)

  val route = add ~ assets
   
  // 路由!!!!
  @ApiOperation(value = "Add integers", notes = "", nickname = "addIntegers", httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
		name = "body", 
		value = "\"numbers\" to sum", 
		required = true,
        dataType = "com.example.akka.add.AddActor$AddRequest", 
		paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Return sum", response = classOf[AddResponse]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def add = path("add") {
    post {
      entity(as[AddRequest]) { request =>
        complete { (addActor ? request).mapTo[AddResponse] }
      }
    }
  }

  def assets = pathPrefix("swagger") {
    getFromResourceDirectory("swagger") ~
    pathSingleSlash(get(redirect("index.html", StatusCodes.PermanentRedirect)))
  }

}

