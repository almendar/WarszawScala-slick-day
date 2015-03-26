package pl.tk.warszawscala.slickday.web.http


import spray.http.HttpHeaders.Location
import spray.http.{HttpResponse, StatusCodes}
import spray.httpx.SprayJsonSupport._
import spray.routing.{ExceptionHandler, HttpService}

import spray.util.LoggingContext
import scala.concurrent.ExecutionContext.Implicits.global

import pl.tk.warszawscala.slickday.web.http.model.{Category, Author, Book, MyJsonProtocol}
import pl.tk.warszawscala.slickday.service.LibraryServiceComponent
import MyJsonProtocol._



trait LibraryHttpService extends HttpService {
  self: LibraryServiceComponent =>

  import spray.json.DefaultJsonProtocol._

  //  implicit def myExceptionHandler(implicit log: LoggingContext) =
  //    ExceptionHandler {
  //      case e:Exception =>
  //        complete(StatusCodes.UnprocessableEntity,s"Already exists")
  //    }

  val serviceRoute =
    pathPrefix("rest") {
      pathPrefix("authors") {
        pathEnd {
          post {
            entity(as[Author]) { author =>
              complete {
                val id = getLibraryService.save(author)
                id.map { idLoct =>
                  HttpResponse(status = StatusCodes.Created, headers = Location(s"/rest/authors/" + idLoct) :: Nil)
                }
              }
            }
          } ~
            get {
              complete {
                getLibraryService.getAllAuthors()
              }
            }
        } ~
          path(LongNumber) { id =>
            get {
              complete {
                getLibraryService.findAuthorById(id)
              }
            } ~
              put {
                entity(as[Author]) { author =>
                  complete {
                    getLibraryService.update(id, author)
                    StatusCodes.Created
                  }
                }
              } ~
              delete {
                complete {
                  getLibraryService.deleteAuthor(id)
                  StatusCodes.NoContent
                }
              }
          }
      } ~
        pathPrefix("categories") {
          pathEnd {
            post {
              entity(as[Category]) { category =>
                complete {
                  val id = getLibraryService.save(category)
                  id.map { idLoct =>
                    HttpResponse(status = StatusCodes.Created, headers = Location(s"/rest/categories/" + idLoct) :: Nil)
                  }
                }
              }
            } ~
              get {
                complete {
                  getLibraryService.getAllCategories()
                }
              }
          } ~
            path(LongNumber) { id =>
              get {
                complete {
                  getLibraryService.findCategoryById(id)
                }
              } ~
              put {
                entity(as[Category]) { category: Category =>
                  complete {
                    getLibraryService.update(id, category)
                    StatusCodes.Created
                  }
                }
              } ~
              delete {
                complete {
                  getLibraryService.deleteCategory(id)
                  StatusCodes.NoContent
                }
              }
            }
        } ~
        pathPrefix("books") {
          pathEnd {
            post {
              entity(as[Book]) { book =>
                complete {
                  val id = getLibraryService.save(book)
                  id.map { idLoct =>
                    HttpResponse(status = StatusCodes.Created, headers = Location(s"/rest/books/" + idLoct) :: Nil)
                  }
                }
              }
            } ~
              get {
                complete {
                  getLibraryService.getAllBooks()
                }
              }
          } ~
            path(LongNumber) { id =>
              get {
                complete {
                  getLibraryService.findBookById(id)
                }
              } ~
                put {
                  entity(as[Book]) { book =>
                    complete {
                      getLibraryService.update(id, book)
                      StatusCodes.Created
                    }
                  }
                } ~
                delete {
                  complete {
                    getLibraryService.deleteBook(id)
                    StatusCodes.NoContent
                  }
                }
            } ~
            path("search") {
              parameters('author.?, 'hashTag.?) { (author: Option[String], hashTag: Option[String]) =>
                complete {
                  getLibraryService.query(author, hashTag)
                }
              }
            }
        }
  } ~
  path("") {
    getFromResource("public/index.html")
  } ~
  getFromResourceDirectory("public")
}
