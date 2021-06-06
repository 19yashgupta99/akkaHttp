package org.knoldus.databaseConnection

import org.knoldus.model.UserType.UserType
import org.knoldus.model.{User, UserType}
import slick.ast.BaseTypedType
import slick.jdbc.MySQLProfile.api._
import slick.jdbc.{JdbcType, MySQLProfile}
import slick.lifted.{ProvenShape, Tag}

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class UserDBSchema(tag: Tag) extends Table[User](tag, "akkahttpusertable") {

  implicit val enumMapper: JdbcType[UserType] with BaseTypedType[UserType] = MappedColumnType.base[UserType ,String](
    category => category.toString,
    string => UserType.withName(string)
  )

  def id : Rep[Option[String]] = column[Option[String]]("id")
  def name: Rep[String] = column[String]("name")
  def category : Rep[UserType] = column[UserType]("category")

  def * : ProvenShape[User] = (id,name,category) <> (User.tupled, User.unapply)
}

class UserDb(db: MySQLProfile.backend.DatabaseDef)(implicit ec: ExecutionContext)
  extends TableQuery(new UserDBSchema(_)){

  implicit val enumMapper: JdbcType[UserType] with BaseTypedType[UserType.Value] = MappedColumnType.base[UserType ,String](
    category => category.toString,
    string => UserType.withName(string)
  )

  def getById(id : Option[String]) : Future[Seq[User]] = {
    db.run[Seq[User]](this.filter(_.id === id).result)
  }

  def insert(user: User) : Future[Int] = {
    db.run(this += user)
  }

  def getAll : Future[Seq[User]] = {
    db.run(this.result)
  }

  def delete(user : User): Future[Int] ={
    db.run(this.filter(_.id === user.id).delete)
  }

  def updateUser(oldUser : User , newUser : User) : Future[Int] ={
    val result = delete(oldUser)
    val finalResult : Future[Int] = result.flatMap[Int]{
      value => {
        if (value > 0) insert(newUser)
        else Future.successful(0)
      }
    }
    finalResult
  }

  def updateUserName(user : User, name :String): Future[Int] = {
    db.run(this.filter(_.id === user.id).map(_.name).update(name))
  }

  def updateUserCategory(user:User , newCategory : UserType.Value): Future[Int] = {
    db.run(this.filter(_.id === user.id).map(_.category).update(newCategory))
  }

}

