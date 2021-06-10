package org.knoldus.service

import org.knoldus.databaseCalls.UserDatabase
import org.knoldus.model.{User, UserType}
import org.knoldus.repository.dao.Dao
import org.mockito.Mockito.{mock, when}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import java.util.UUID.randomUUID
import scala.concurrent.Future

class UserServiceTest extends AsyncFlatSpec with Matchers with ScalaFutures{
  val mockedUserDatabse: Dao[User] = mock(classOf[UserDatabase])
  val userService = new UserService(mockedUserDatabse)

  it should "create a new User" in{
    val user: User = User(Some(randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.createUser(user)) thenReturn Future.successful(true)
    whenReady(userService.createNewUser(user)){
      result => result shouldBe true
    }
  }

  it should "not create a new user" in{
    val user: User = User(null, "Yash", UserType.Admin)
    when(mockedUserDatabse.createUser(user)) thenReturn Future.successful(true)
    whenReady(userService.createNewUser(user)){
      result => result shouldBe false
    }
  }

  it should "return a List[User]" in {
    when(mockedUserDatabse.listAllUser()) thenReturn Future.successful(List())
    whenReady(userService.getAllUser){
      result => result shouldBe List()
    }
  }

  it should "update the existing user by new user" in{
    val user = User(Some(randomUUID().toString), "Yash", UserType.Admin)
    val newUser = User(Some(randomUUID().toString), "Rudra", UserType.Admin)
    when(mockedUserDatabse.updateUser(user,newUser)) thenReturn Future.successful(true)
    whenReady(userService.updateUser(user,newUser)){
      result => result shouldBe true
    }
  }

  it should "not update the existing user by new user" in{
    val user = User(Some(randomUUID().toString), "Yash", UserType.Admin)
    val newUser = user
    when(mockedUserDatabse.updateUser(user,newUser)) thenReturn Future.successful(true)
    whenReady(userService.updateUser(user,newUser)){
      result => result shouldBe false
    }
  }

  it should "update the name of the existing user" in{
    val user = User(Some(randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.updateUserName(user,"YashGupta")) thenReturn Future.successful(true)
    whenReady(userService.updateUserName(user,"YashGupta")){
      result => result shouldBe true
    }
  }

  it should "not update the name of the existing user" in{
    val user = User(Some(randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.updateUserName(user,"Yash")) thenReturn Future.successful(true)
    whenReady(userService.updateUserName(user,"Yash")){
      result => result shouldBe false
    }
  }

  it should "update the category of the existing user" in{
    val user = User(Some(randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.updateUserCategory(user,UserType.Customer)) thenReturn Future.successful(true)
    whenReady(userService.updateUserCategory(user,UserType.Customer)){
      result => result shouldBe true
    }
  }


  it should "not update the category of the existing user" in{
    val user = User(Some(randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.updateUserCategory(user,UserType.Admin)) thenReturn Future.successful(true)
    whenReady(userService.updateUserCategory(user,UserType.Admin)){
      result => result shouldBe false
    }
  }

  it should "delete an existing user" in{
    val user = User(Some(randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.deleteUser(user)) thenReturn Future.successful(true)
    whenReady(userService.deleteUser(user)){
      result => result shouldBe true
    }
  }

  it should "get the user by id" in{
    val user = User(Some(randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.getUserById(user.id)) thenReturn Future.successful(user)
    whenReady(userService.getById(user.id)){
      result => result shouldBe user
    }
  }

}
