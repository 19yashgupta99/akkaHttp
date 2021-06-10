package org.knoldus.databaseCalls

import org.knoldus.model.{User, UserType}
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers

import java.util.UUID.randomUUID
import scala.concurrent.duration.DurationInt

class UserDatabaseTest extends AsyncFunSuite with Matchers with ScalaFutures{

  val userDatabase = new UserDatabase
  val user: User = User(Some(randomUUID().toString), "Yash123", UserType.Admin)
  val newUser: User = User(Some(randomUUID().toString), "Rudra", UserType.Admin)
  implicit val timeout: Timeout = Timeout(5.seconds)

  test("A user should be created"){
    whenReady(userDatabase.createUser(user),timeout) {
      result => result shouldBe true
    }
  }


  test("It should update a new user with existing user"){
    whenReady(userDatabase.updateUser(user,newUser),timeout) {
      result => result shouldBe true
    }
  }


  test("it should update the username of existing user"){
    whenReady(userDatabase.updateUserName(newUser,"Honey"),timeout) {
      result => result shouldBe true
    }
  }

  test("it should update the category of the existing user"){
    whenReady(userDatabase.updateUserCategory(newUser,UserType.Customer),timeout) {
      result => result shouldBe true
    }
  }

  test("It should get user by its ID"){
    whenReady(userDatabase.getUserById(newUser.id),timeout){
      result => result shouldBe User(newUser.id, "Honey", UserType.Customer)
    }
  }

  test("it should delete the existing user"){
    whenReady(userDatabase.deleteUser(User(newUser.id, "Honey", UserType.Customer)),timeout){
      result => result shouldBe true
    }
  }
}