package org.knoldus.model

import org.knoldus.model.UserType.UserType
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, JsonFormat, RootJsonFormat}



  case class User(
                   id       : Option[String],
                   name     : String,
                   category : UserType
                 )


trait UserJsonProtocol extends DefaultJsonProtocol {

  implicit object UserTypeJsonFormat extends JsonFormat[UserType.Value] {
    def write(obj: UserType.Value): JsValue = JsString(obj.toString)
    def read(json: JsValue): UserType.Value = json match {
      case JsString(str) => UserType.withName(str)
      case _             => throw DeserializationException("Enum string expected")
    }
  }


  implicit val playerFormat: RootJsonFormat[User] = jsonFormat3(User)
}