package pl.tk.warszawscala.slickday.service

import pl.tk.warszawscala.slickday.web.http.model.{Category, Book, Author}
import pl.tk.warszawscala.slickday.repository.SlickLibraryRepositoryComponent
import slick.dbio._

import scala.concurrent.Future

trait DatabaseLibraryServiceComponent extends LibraryServiceComponent { self : SlickLibraryRepositoryComponent =>
  override def getLibraryService : LibraryService = new DataBaseNoteService

  private class DataBaseNoteService extends LibraryService {
    override def save(author: Author): Future[Long] = ???

    override def findAuthorById(id: Long): Future[Option[Author]] = ???

    override def findCategoryById(id: Long): Future[List[Category]] = ???

    override def findBookById(id: Long): Future[Option[Book]] = ???

    override def getAllBooks(): Future[List[Book]] = ???

    override def update(id: Long, author: Author): Unit = ???

    override def update(id: Long, book: Book): Unit = ???

    override def update(id: Long, category: Category): Unit = ???

    override def getAllCategories(): Future[List[Category]] = ???

    override def save(book: Book): Future[Long] = ???

    override def save(category: Category): Future[Long] = ???

    override def query(author: Option[String], category: Option[String]): Future[List[Book]] = ???

    override def getAllAuthors(): Future[List[Author]] = ???

    override def deleteCategory(l: Long): Unit = ???

    override def deleteAuthor(l: Long): Unit = ???

    override def deleteBook(l: Long): Unit = ???
  }
}