package pl.tk.warszawscala.slickday.web.http


import spray.http.HttpHeaders.Location
import spray.http.{HttpResponse, StatusCodes}
import spray.httpx.SprayJsonSupport._
import spray.routing.{ExceptionHandler, HttpService}

import spray.util.LoggingContext
import scala.concurrent.ExecutionContext.Implicits.global

import pl.tk.warszawscala.slickday.web.http.model.{MyJsonProtocol, Note}
import pl.tk.warszawscala.slickday.web.service.NotesServiceComponent
import MyJsonProtocol._



trait SimpleHttpService extends HttpService { self:NotesServiceComponent =>
  import spray.json.DefaultJsonProtocol._

  //  implicit def myExceptionHandler(implicit log: LoggingContext) =
//    ExceptionHandler {
//      case e:Exception =>
//        complete(StatusCodes.UnprocessableEntity,s"Already exists")
//    }

  val serviceRoute =
    pathPrefix("notes") {
      pathEnd {
        post {
          entity(as[Note]) { note =>
  //          val (isValid,errorMessages) = Note.validator(invitation)
  //          validate(isValid,errorMessages.mkString(",")) {
              complete {
                val id = getNoteService.save(note)
                HttpResponse(status = StatusCodes.Created,headers = Location(s"notes/" + id)::Nil)
              }
  //          }
          }
        } ~
        get {
          complete {
            getNoteService.getAll
          }
        }
      } ~
      path(LongNumber) { id =>
        get {
          complete {
            getNoteService.find(id)
          }
        } ~
        put {
          entity(as[Note]) {note =>
            complete {
              getNoteService.update(id,note)
              StatusCodes.Created
            }
          }
        }
      } ~
      path("search") {
        parameters('author.?, 'hashTag.?) { (author: Option[String], hashTag: Option[String]) =>
          complete {
            getNoteService.query(author,hashTag)
          }
        }
      }
    }
}

