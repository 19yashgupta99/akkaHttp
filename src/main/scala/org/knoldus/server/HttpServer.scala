package org.knoldus.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.knoldus.databaseCalls.UserDatabase
import org.knoldus.routes.Routes
import org.knoldus.service.UserService

object HttpServer extends App {
  implicit val system: ActorSystem = ActorSystem("MarshallingJSON")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val routes = new Routes(new UserService(new UserDatabase))

  Http().bindAndHandle(routes.userManagementRouteSkeleton, "localhost", 8080)

}
