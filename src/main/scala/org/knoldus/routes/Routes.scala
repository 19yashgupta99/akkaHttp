package org.knoldus.routes


import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import org.knoldus.databaseCalls.UserDatabase
import org.knoldus.model.{User, UserJsonProtocol, UserType}
import org.knoldus.service.UserService

import scala.util.{Failure, Success}

class Routes(userService: UserService) extends UserJsonProtocol with SprayJsonSupport {

  //val userService = new UserService(new UserDatabase)

  val userManagementRouteSkeleton: Route =
    pathPrefix("api" / "user") {
      get {
          path("getAllUsers"){
            val users = userService.getAllUser
            complete(users)
          } ~
          (path(Segment) | parameter('id)) { id =>
            val user = userService.getById(Some(id))
            complete(user)
          }
      }~
      post {
        path("create"){
          entity(implicitly[FromRequestUnmarshaller[User]]) { user =>
            if(user.id.isEmpty){
              val newUser = User(Some(java.util.UUID.randomUUID().toString),user.name,user.category)
              println(newUser)
              onComplete(userService.createNewUser(newUser)){
                case Success(value) =>
                  if(value) complete("The user is created successfully")
                  else complete("the user is not created successfully")
                case Failure(ex)    => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
              }
            }else{
              onComplete(userService.createNewUser(user)){
                case Success(value) =>
                  if(value) complete("The user is created successfully")
                  else complete("the user is not created successfully")
                case Failure(ex)    => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
              }
            }
          }
        }~
          path("update"){
            entity(implicitly[FromRequestUnmarshaller[List[User]]]) { users =>
              if(users.head.id.isEmpty || users(1).id.isEmpty){
                complete(StatusCodes.BadRequest)
              }
              else {
                onComplete(userService.updateUser(users.head,users(1))){
                  case Success(value) =>
                    if(value) complete("The user is updated successfully")
                    else complete("The user is not updated successfully")
                  case Failure(ex)    => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
                }

              }
            }
          }~
          path("update" / "name" / Segment){
            name =>
              entity(implicitly[FromRequestUnmarshaller[User]]) {
                user =>
                  if(user.id.isEmpty) complete(InternalServerError, s"please provide the id of the user")
                  else{
                    onComplete(userService.updateUserName(user,name)){
                      case Success(value) =>
                        if(value) complete("The name of the given user is updated successfully")
                        else complete("The name of the given user is not update successfully")
                      case Failure(ex)    => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
                    }
                  }
              }
          }~
          path("update" / "category" / Segment){
            category =>
              entity(implicitly[FromRequestUnmarshaller[User]]){
                user =>
                  if(user.id.isEmpty) complete(InternalServerError, s"please provide the id of the user")
                  else {
                    onComplete(userService.updateUserCategory(user,UserType.withName(category))){
                      case Success(value) =>
                        if(value) complete("The category of the user is updated successfully")
                        else complete("The category of the user is not updated successfully")
                      case Failure(ex)    => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
                    }
                  }
              }
          }
      } ~
        delete {
          path("delete"){
            entity(as[User]) { user =>
              if(user.id.isEmpty) complete(InternalServerError, s"please provide the id of the user")
              else{
                onComplete(userService.deleteUser(user)){
                  case Success(value) =>
                    if(value) complete("The user is deleted successfully")
                    else complete("The user is not deleted")
                  case Failure(ex)    => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
                }
              }
            }
          }
        }
    }

}
