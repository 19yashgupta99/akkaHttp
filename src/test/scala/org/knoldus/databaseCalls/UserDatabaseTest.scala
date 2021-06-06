package org.knoldus.databaseCalls

import org.knoldus.model.{User, UserType}
import org.scalatest.flatspec.AsyncFlatSpec

class UserDatabaseTest extends AsyncFlatSpec{


  it should "create the new User " in {
    val userDatabase = new UserDatabase
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    for{
      result <- userDatabase.createUser(user)
    }yield assert(result)
  }

  it should "return a list[User]" in{
    val userDatabase = new UserDatabase
    for{
      resultList <- userDatabase.listAllUser()
    }yield {
      assert(resultList.nonEmpty)
    }
  }

  it should "update a new user with existing user " in {
    val userDatabase = new UserDatabase
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    val newUser = User(Some(java.util.UUID.randomUUID().toString), "Rudra", UserType.Admin)
    for{
      oldUserCreated <- userDatabase.createUser(user)
      isUserUpdated <- userDatabase.updateUser(user,newUser)
    }yield {
      assert(isUserUpdated)
    }
  }



  it should "update the username of existing user" in{
    val userDatabase = new UserDatabase
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    for{
      userCreated <- userDatabase.createUser(user)
      isUserNameUpdated <- userDatabase.updateUserName(user,"YashGupta")
    } yield {
      assert(isUserNameUpdated)
    }
  }

  it should "update the category of the existing user" in{
    val userDatabase = new UserDatabase
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    for {
      userCreated <- userDatabase.createUser(user)
      isUserCategoryUpdated <- userDatabase.updateUserCategory(user,UserType.Customer)
    }yield {
      assert(isUserCategoryUpdated)
    }
  }

  it should "delete the existing user" in{
    val userDatabase = new UserDatabase
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    for {
      userCreated <- userDatabase.createUser(user)
      isUserDeleted <- userDatabase.deleteUser(user)
    }yield {
      assert(isUserDeleted)
    }
  }

  it should "get the user by its ID" in{
    val userDatabase = new UserDatabase
    val user = User(Some(java.util.UUID.randomUUID().toString), "Yash", UserType.Admin)
    for {
      userCreated <- userDatabase.createUser(user)
      obtainedUser <- userDatabase.getUserById(user.id)
    }yield assert(obtainedUser == user)
  }

}
