package org.knoldus.service

import org.knoldus.model.{User, UserType}
import org.knoldus.repository.dao.Dao

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserService(userDatabase: Dao[User]){
  def createNewUser(user: User): Future[Boolean] = {
    if(user.id != null && user.name.nonEmpty && UserType.values.contains(user.category)){
      userDatabase.createUser(user)
    }
    else Future(false)
  }

  def getAllUser: Future[List[User]] ={
    userDatabase.listAllUser()
  }

  def updateUser(oldUser : User, newUser : User):Future[Boolean] ={
    if(oldUser != newUser){
      userDatabase.updateUser(oldUser,newUser)
    }
    else Future(false)
  }

  def updateUserName(user : User, newName : String):Future[Boolean] ={
    if(user.name != newName && newName.nonEmpty){
      userDatabase.updateUserName(user,newName)
    }
    else {
      Future(false)
    }
  }

  def updateUserCategory(user:User , newCategory : UserType.Value): Future[Boolean] ={
    if(user.category != newCategory && UserType.values.contains(newCategory)){
      userDatabase.updateUserCategory(user , newCategory)
    }
    else Future(false)
  }

  def deleteUser(user : User) : Future[Boolean]={
    userDatabase.deleteUser(user)
  }

  def getById(id: Option[String]): Future[User] ={
    userDatabase.getUserById(id)
  }
}
