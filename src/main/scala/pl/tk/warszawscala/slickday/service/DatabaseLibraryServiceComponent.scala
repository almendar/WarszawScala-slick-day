package pl.tk.warszawscala.slickday.service

import pl.tk.warszawscala.slickday.web.http.model.{Category, Book, Author}
import pl.tk.warszawscala.slickday.repository.SlickLibraryRepositoryComponent
import slick.dbio._

import scala.concurrent.Future

trait DatabaseLibraryServiceComponent extends LibraryServiceComponent { self : SlickLibraryRepositoryComponent =>
  override def getLibraryService : NoteService = new DataBaseNoteService

  private class DataBaseNoteService extends NoteService {
    override def save(note: Author): Future[Long] = ???

    override def findAuthorById(id: Long): Future[Option[Author]] = ???

    override def findCategoryById(id: Long): Future[List[Category]] = ???

    override def findBookById(id: Long): Future[Option[Book]] = ???

    override def getAllBooks(): Future[List[Book]] = ???

    override def update(id: Long, note: Author): Unit = ???

    override def update(id: Long, note: Book): Unit = ???

    override def update(id: Long, note: Category): Unit = ???

    override def getAllCategories(): Future[List[Category]] = ???

    override def save(hashtag: Book): Future[Long] = ???

    override def save(author: Category): Future[Long] = ???

    override def query(author: Option[String], category: Option[String]): Future[List[Book]] = ???

    override def getAllAuthors(): Future[List[Author]] = ???
  }
}