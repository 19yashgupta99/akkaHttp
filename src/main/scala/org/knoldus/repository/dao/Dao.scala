package org.knoldus.repository.dao

import org.knoldus.model.{User, UserType}

import java.util.UUID
import scala.concurrent.Future

trait Dao[T] {

  def createUser(obj : T) : Future[Boolean]

  def listAllUser():Future[List[T]]

  def updateUser(oldObject: T, newObject:T): Future[Boolean]

  def deleteUser(obj : T) : Future[Boolean]

  def updateUserName(obj : T , newName : String) : Future[Boolean]

  def updateUserCategory(obj :T , newCategory :UserType.Value) : Future[Boolean]

  def getUserById(id: Option[String]) :Future[User]

}
