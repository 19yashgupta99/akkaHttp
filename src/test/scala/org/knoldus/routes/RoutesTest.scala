package org.knoldus.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.knoldus.model.UserType.Customer
import org.knoldus.model.{User, UserJsonProtocol, UserType}
import org.knoldus.service.UserService
import org.mockito.Mockito.{mock, when}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import java.util.UUID.randomUUID
import scala.concurrent.Future
import scala.language.postfixOps

class RoutesTest extends AsyncFlatSpec with Matchers with ScalatestRouteTest with UserJsonProtocol with SprayJsonSupport{

  val mockedUserService: UserService = mock(classOf[UserService])
  val route = new Routes(mockedUserService)
  val user: User = User(Some("abfba0fa-beef-4db9-b19a-78f8b7cdfacc"),"yash_gupta",UserType.Admin)

  "The route" should "return all the user" in{
    when(mockedUserService.getAllUser) thenReturn Future.successful(List(user))
    Get("/api/user/getAllUsers") ~> route.userManagementRouteSkeleton ~> check{
      status shouldBe StatusCodes.OK
      responseAs[List[User]] shouldBe List(user)
    }
  }

  "The route" should "return the user with the given ID" in{
    when(mockedUserService.getById(Some("abfba0fa-beef-4db9-b19a-78f8b7cdfacc"))) thenReturn Future.successful(user)
    Get("/api/user/abfba0fa-beef-4db9-b19a-78f8b7cdfacc") ~> route.userManagementRouteSkeleton ~> check{
      status shouldBe StatusCodes.OK
      responseAs[User] shouldBe user
    }
  }

  "The route" should "create the new user in database" in{
    when(mockedUserService.createNewUser(user)) thenReturn Future.successful(true)
    Post("/api/user/create", user) ~> route.userManagementRouteSkeleton ~> check{
      status shouldBe StatusCodes.OK
      responseAs[String] shouldBe "The user is created successfully"
    }
  }

  "The route" should "update existing user with new user" in {
    val newUser = User(Some(randomUUID().toString),"rudra_dupta",Customer)
    when(mockedUserService.updateUser(user,newUser)) thenReturn Future.successful(true)
    Post("/api/user/update", List(user,newUser)) ~> route.userManagementRouteSkeleton ~> check{
      status shouldBe StatusCodes.OK
      responseAs[String] shouldBe "The user is updated successfully"
    }
  }

  "The route" should "update existing user name with newName" in {
    when(mockedUserService.updateUserName(user,"RudraGupta")) thenReturn Future.successful(true)
    Post("/api/user/update/name/RudraGupta", user) ~> route.userManagementRouteSkeleton ~> check{
      status shouldBe StatusCodes.OK
      responseAs[String] shouldBe "The name of the given user is updated successfully"
    }
  }

  "The route" should "update existing user category with new category" in {
    when(mockedUserService.updateUserCategory(user,Customer)) thenReturn Future.successful(true)
    Post("/api/user/update/category/Customer", user) ~> route.userManagementRouteSkeleton ~> check{
      status shouldBe StatusCodes.OK
      responseAs[String] shouldBe "The category of the user is updated successfully"
    }
  }

  "The route" should "delete existing user from database" in {
    when(mockedUserService.deleteUser(user)) thenReturn Future.successful(true)
    Delete("/api/user/delete", user) ~> route.userManagementRouteSkeleton ~> check{
      status shouldBe StatusCodes.OK
      responseAs[String] shouldBe "The user is deleted successfully"
    }
  }
}
