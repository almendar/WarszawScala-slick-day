package pl.tk.warszawscala.slickday.service

import java.time.{ZonedDateTime, LocalDateTime}
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock

import pl.tk.warszawscala.slickday.web.http.model._
import pl.tk.warszawscala.slickday.repository.{MockLibraryRepositoryComponent, LibraryRepositoryComponent}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by tomaszk on 3/19/15.
 */

trait MockLibraryServiceComponent extends LibraryServiceComponent with MockLibraryRepositoryComponent {


  override val getLibraryService: NoteService = new MockNoteService

//  override val getLibraryRepository = new MockLibraryrRepositoryComponent {}

  private class MockNoteService extends NoteService {

    override def save(author: Author): Future[Long] = Future.successful(getLibraryRepository.authorRepo.persist(author))

    override def save(book: Book): Future[Long] = Future.successful(getLibraryRepository.bookRepo.persist(book))

    override def save(category: Category): Future[Long] = Future.successful(getLibraryRepository.categoryRepo.persist(category))

    override def findAuthorById(id: Long): Future[Option[Author]] = Future.successful(getLibraryRepository.authorRepo.get(id))

    override def findCategoryById(id: Long): Future[List[Category]] = Future.successful(getLibraryRepository.categoryRepo.get(id))

    override def findBookById(id: Long): Future[Option[Book]] = Future.successful(getLibraryRepository.bookRepo.get(id))

    override def update(id: Long, entity: Author): Unit = Future.successful(getLibraryRepository.authorRepo.update(id, entity))

    override def update(id: Long, entity: Book): Unit = Future.successful(getLibraryRepository.bookRepo.update(id, entity))

    override def update(id: Long, entity: Category): Unit = Future.successful(getLibraryRepository.categoryRepo.update(id, entity))

    override def getAllBooks(): Future[List[Book]] = Future.successful(getLibraryRepository.bookRepo.store)

    override def getAllCategories(): Future[List[Category]] = Future.successful(getLibraryRepository.categoryRepo.store.filter{ cat =>
      cat.parentId == None
    }.map{cat =>
      if(getLibraryRepository.categoryRepo.store.exists(_.parentId == cat.getId)) cat.copy(hasChildren = true)
      else cat
    })

    override def getAllAuthors(): Future[List[Author]] = Future.successful(getLibraryRepository.authorRepo.store)

    override def query(author: Option[String], category: Option[String]): Future[List[Book]] = {
      val meetsPredicate: (Option[String] => String => Boolean) = { p => k =>
        p match {
          case Some(s) => k == s
          case None => true
        }
      }
      Future.successful {
        getLibraryRepository.bookRepo.store.filter { book =>
          book.authors.exists { thisAuthor => meetsPredicate(author)(thisAuthor.name) } &&
            meetsPredicate(category)(book.category.name)
        }
      }

    }

    override def deleteCategory(l: Long): Unit = getLibraryRepository.categoryRepo.delete(l)

    override def deleteAuthor(l: Long): Unit = getLibraryRepository.authorRepo.delete(l)

    override def deleteBook(l: Long): Unit = getLibraryRepository.bookRepo.delete(l)
  }
}