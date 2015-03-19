package pl.tk.warszawscala.slickday.web.service

import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock

import pl.tk.warszawscala.slickday.web.http.model.{Hashtag, Author, Note}
import pl.tk.warszawscala.slickday.web.repository.NotesRepositoryComponent
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by tomaszk on 3/19/15.
 */
trait MockNotesServiceComponent extends NotesServiceComponent with NotesRepositoryComponent {

  override val getNoteService : NoteService = new MockNoteService

  /**
   * We don't need this in mock
   * @return
   */
  override def getNotesRepository = null

  private class MockNoteService extends NoteService {

    val lock = new ReentrantLock()
    var notes = Map.empty[String, Note]
    var idGenerator: AtomicLong = new AtomicLong(0);


    override def save(note: Note): Future[String] = {
      val newId = idGenerator.getAndIncrement.toString;
      lock.lock()
     notes = notes.updated(newId.toString, note.copy(id = Some(newId)))
      lock.unlock()
      Future.successful(newId)
    }

    override def update(id: String, note: Note): Unit = {
      lock.lock()
      notes = notes.updated(id.toString, note.copy(id = Some(id),lastModified = LocalDateTime.now()))
      lock.unlock()
    }

    override def find(id: String): Future[Option[Note]] = Future.successful(notes.get(id))

    override def getAll(): Future[List[Note]] = Future.successful(notes.values.toList)

    override def query(author: Option[String], hashtag: Option[String]): Future[List[Note]] = {

      val pred  : Option[String] =>  String => Boolean = input => p => input match {
        case Some(l) => p == l
        case None => true
      }
      Future.successful {
      notes.values.toList.filter{note : Note =>
        val authorFits : Boolean =  (note.owner :: note.coAuthors).exists { a: Author =>
          pred(author)(a.login)
        }
        val hashTagFits : Boolean = note.hashtags.exists{h:Hashtag => pred(hashtag)(h.value)}
        authorFits && hashTagFits
      }
    }
    }
  }

}
