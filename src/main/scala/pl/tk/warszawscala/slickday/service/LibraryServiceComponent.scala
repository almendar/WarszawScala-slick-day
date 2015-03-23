package pl.tk.warszawscala.slickday.service

import pl.tk.warszawscala.slickday.web.http.model.{Category, Book, Author}
import slick.dbio.DBIO

import scala.concurrent.Future

import pl.tk.warszawscala.slickday.repository.{LibraryRepositoryComponent, SlickLibraryRepositoryComponent}
/**
 * Created by tomaszk on 3/17/15.
 */
trait LibraryServiceComponent { self  : LibraryRepositoryComponent =>

  def getLibraryService : NoteService

  trait NoteService {

    def save(author: Author) : Future[Long]

    def save(book: Book) : Future[Long]

    def save(category: Category) : Future[Long]

    def query(author: Option[String], category: Option[String]): Future[List[Book]]

    def update(id:Long, note:Author)
    def update(id:Long, note:Book)
    def update(id:Long, note:Category)

    def getAllBooks(): Future[List[Book]]
    def getAllCategories() : Future[List[Category]]
    def getAllAuthors() : Future[List[Author]]

    def findBookById(id:Long) : Future[Option[Book]]
    def findCategoryById(id:Long) : Future[List[Category]]
    def findAuthorById(id:Long) : Future[Option[Author]]
  }
}














