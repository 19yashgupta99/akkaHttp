package org.knoldus.service

import org.knoldus.databaseCalls.UserDatabase
import org.knoldus.model.{User, UserType}
import org.knoldus.repository.dao.Dao
import org.mockito.Mockito.{mock, when}
import org.scalatest.flatspec.AsyncFlatSpec
import scala.concurrent.Future

class UserServiceTest extends AsyncFlatSpec{
  val mockedUserDatabse: Dao[User] = mock(classOf[UserDatabase])

  it should "create a new user" in{
    val userService = new UserService(mockedUserDatabse)
    val user: User = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.createUser(user)) thenReturn Future(true)
    for{
      result <-userService.createNewUser(user)
    }yield assert(result)
  }

  it should "not create a new user" in{
    val userService = new UserService(mockedUserDatabse)
    val user: User = User(null, "Yash", UserType.Admin)
    when(mockedUserDatabse.createUser(user)) thenReturn Future(false)
    for{
      result <- userService.createNewUser(user)
    }yield assert(!result)
  }

  it should "return a List[User]" in {
    val userService = new UserService(mockedUserDatabse)
    when(mockedUserDatabse.listAllUser()) thenReturn Future(List())
    for{
      allUser <- userService.getAllUser
    }yield assert(allUser == List())
  }

  it should "update the existing user by new user" in{
    val userService = new UserService(mockedUserDatabse)
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    val newUser = User(Some(java.util.UUID.randomUUID().toString), "Rudra", UserType.Admin)
    when(mockedUserDatabse.updateUser(user,newUser)) thenReturn Future(true)
    for{
      isUserUpdated <- userService.updateUser(user,newUser)
    }yield assert(isUserUpdated)
  }

  it should "not update the existing user by new user" in{
    val userService = new UserService(mockedUserDatabse)
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    val newUser = user
    when(mockedUserDatabse.updateUser(user,newUser)) thenReturn Future(true)
    for{
      isUserUpdated <- userService.updateUser(user,newUser)
    }yield assert(!isUserUpdated)
  }

  it should "update the name of the existing user" in{
    val userService = new UserService(mockedUserDatabse)
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.updateUserName(user,"YashGupta")) thenReturn Future(true)
    for{
      isUserNameUpdated <- userService.updateUserName(user,"YashGupta")
    }yield assert(isUserNameUpdated)
  }

  it should "not update the name of the existing user" in{
    val userService = new UserService(mockedUserDatabse)
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.updateUserName(user,"Yash")) thenReturn Future(true)
    for{
      isUserNameUpdated <- userService.updateUserName(user,"Yash")
    }yield assert(!isUserNameUpdated)
  }

  it should "update the category of the existing user" in{
    val userService = new UserService(mockedUserDatabse)
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.updateUserCategory(user,UserType.Customer)) thenReturn Future(true)
    for{
      isUserCategoryUpdated <- userService.updateUserCategory(user,UserType.Customer)
    }yield assert(isUserCategoryUpdated)
  }


  it should "not update the category of the existing user" in{
    val userService = new UserService(mockedUserDatabse)
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.updateUserCategory(user,UserType.Admin)) thenReturn Future(true)
    for{
      isUserCategoryUpdated <- userService.updateUserCategory(user,UserType.Admin)
    }yield assert(!isUserCategoryUpdated)
  }

  it should "delete an existing user" in{
    val userService = new UserService(mockedUserDatabse)
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.deleteUser(user)) thenReturn Future(true)
    for{
      isUserDeleted <- userService.deleteUser(user)
    }yield assert(isUserDeleted)
  }

  it should "get the user by id" in{
    val userService = new UserService(mockedUserDatabse)
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    when(mockedUserDatabse.getUserById(user.id)) thenReturn Future(user)
    for{
      obtainedUser <- userService.getById(user.id)
    }yield assert(obtainedUser == user)
  }

}
