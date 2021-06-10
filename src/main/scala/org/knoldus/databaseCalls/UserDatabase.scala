package org.knoldus.databaseCalls

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import org.knoldus.databaseCalls.ActorModelMessage._
import org.knoldus.databaseConnection.{DBConnection, UserDb}
import org.knoldus.model.{User, UserType}
import org.knoldus.repository.dao.Dao

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object ActorModelMessage{
  case class CreateUser(user:User)
  case object ListAllUsers
  case class UpdateUser(oldUser : User, newUser : User)
  case class UpdateUserName(user: User, newName: String)
  case class UpdateUserCategory(user: User, newCategory: UserType.Value)
  case class DeleteUser(user: User)
  case class GetUserByID(id: Option[String])
}


class ActorModel(userDbObject: UserDb) extends Actor{

  /*val dbConnection = new DBConnection
  val userDbObject = new UserDb(dbConnection.db)
*/
  override def receive: Receive = crudOperations()

  def crudOperations() :Receive ={
    case CreateUser(user) =>
      val res   = {
            val result = userDbObject.insert(user)
            result.map{
              value => {
                if(value > 0) true
                else false
              }
            }
      }.recover{
        case _:RuntimeException =>
          false
      }
      res.pipeTo(sender())

    case ListAllUsers =>
        val result = {
          userDbObject.getAll
        }.recover{
          case _:RuntimeException => List()
        }
        val finalResult =result.map{
          seq =>
            seq.toList
        }.recover{
          case _:RuntimeException => List()
        }
      finalResult.pipeTo(sender())

    case UpdateUser(oldUser,newUser) =>
      val res =  {
          val result = userDbObject.updateUser(oldUser,newUser)
          result.map{
            value =>{
              if(value > 0) true
              else false
            }
          }
      }.recover{
        case _:RuntimeException => false
      }
      res.pipeTo(sender())

    case UpdateUserName(user,newName) =>
      val res = {
        val result = userDbObject.updateUserName(user,newName)
        result.map{
          value =>{
            if(value > 0) true
            else false
          }
        }
      }.recover{
        case _:RuntimeException => false
      }
      res.pipeTo(sender())

    case UpdateUserCategory(user, newCategory) =>
      val res = {
        val result = userDbObject.updateUserCategory(user,newCategory)
        result.map{
          value =>{
            if(value > 0) true
            else false
          }
        }
      }.recover{
        case _:RuntimeException => false
      }
      res.pipeTo(sender())
    case DeleteUser(user) =>
      val res = {
        val result = userDbObject.delete(user)
        result.map{
          value =>{
            if(value > 0) true
            else false
          }
        }
      }.recover{
        case _:RuntimeException => false
      }
      res.pipeTo(sender())

    case GetUserByID(id) =>
      val result = {
        userDbObject.getById(id)
      }.recover{
        case _:RuntimeException => List()
      }
      val secondResult =result.map{
        seq =>
          seq.toList
      }.recover{
        case _:RuntimeException => List()
      }
      val finalResult = secondResult.map{
        user => user.head
      }
      finalResult.pipeTo(sender())

  }
}

class UserDatabase extends Dao[User]{

  val system: ActorSystem = ActorSystem("System")
  val dbConnection = new DBConnection
  val actor: ActorRef = system.actorOf(Props(new ActorModel(userDbObject = new UserDb(dbConnection.db))))
  implicit val timeout: Timeout = Timeout(5 seconds)

  override def createUser(user: User): Future[Boolean] =
    (actor ? CreateUser(user)).mapTo[Boolean]

  override def listAllUser(): Future[List[User]] =
    (actor ? ListAllUsers).mapTo[List[User]]

  override def updateUser(oldUser : User, newUser : User): Future[Boolean] =
    (actor ? UpdateUser(oldUser,newUser)).mapTo[Boolean]

  override def updateUserName(user: User, newName: String): Future[Boolean] =
    (actor ? UpdateUserName(user,newName)).mapTo[Boolean]

  override def updateUserCategory(user: User, newCategory: UserType.Value): Future[Boolean] =
    (actor ? UpdateUserCategory(user, newCategory)).mapTo[Boolean]

  override def deleteUser(user: User): Future[Boolean] =
    (actor ? DeleteUser(user)).mapTo[Boolean]

  override def getUserById(id: Option[String]): Future[User] =
    (actor ? GetUserByID(id)).mapTo[User]
}
