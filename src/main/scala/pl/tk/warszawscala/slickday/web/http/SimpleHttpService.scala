package pl.tk.warszawscala.slickday.web.http

import pl.tk.warszawscala.slickday.web.model.{JsonProtocol, Note}
import pl.tk.warszawscala.slickday.web.persistence.{DataBaseProvider, Database}
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport._
import spray.routing.{ExceptionHandler, HttpService}
import JsonProtocol._
import Database.AlreadyInDataBaseException

import spray.util.LoggingContext

/**
 * Created by tomaszk on 3/12/15.
 */



trait SimpleHttpService extends HttpService { self:DataBaseProvider[Note] =>
  implicit def myExceptionHandler(implicit log: LoggingContext) =
    ExceptionHandler {
      case e:AlreadyInDataBaseException =>
        complete(StatusCodes.UnprocessableEntity,s"Already exists")
    }

  val serviceRoute = {
    path("invitation") {
      post {
        entity(as[Note]) { invitation =>
//          val (isValid,errorMessages) = Note.validator(invitation)
//          validate(isValid,errorMessages.mkString(",")) {
            complete {
              getDataBase.insert(invitation)
              StatusCodes.Created
            }
//          }
        }
      } ~
      get {
        complete{
          getDataBase.getAll
        }
      }
    }

  }

}

