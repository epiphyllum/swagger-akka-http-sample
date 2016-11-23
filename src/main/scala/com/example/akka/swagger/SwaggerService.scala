package com.example.akka.swagger

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import akka.util.Timeout
import com.example.akka.DefaultJsonFormats
import io.swagger.annotations._

import scala.concurrent.ExecutionContext


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
class SwaggerService extends Directives with DefaultJsonFormats {
  val route = assets
  def assets = pathPrefix("swagger") {
    getFromResourceDirectory("swagger") ~
    pathSingleSlash(get(redirect("index.html", StatusCodes.PermanentRedirect)))
  }
}

