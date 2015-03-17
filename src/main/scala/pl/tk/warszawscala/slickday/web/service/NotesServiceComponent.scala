package pl.tk.warszawscala.slickday.web.service

import slick.dbio.DBIO

import scala.concurrent.Future

import pl.tk.warszawscala.slickday.web.http.model.{Hashtag, Author, Note}
import pl.tk.warszawscala.slickday.web.repository.{NotesRepositoryComponent}
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by tomaszk on 3/17/15.
 */
trait NotesServiceComponent { self  : NotesRepositoryComponent =>

  def getNoteService : NoteService

  trait NoteService {
    def save(note: Note) : Long

    def query(author: Option[String], hashtag: Option[String]): Future[List[Note]]

    def update(l: Long, note: Note)

    def getAll(): Future[List[Note]]

    def find(id:Long) : Future[Option[Note]]
  }
}






trait DatabaseNotesServiceComponent extends NotesServiceComponent { self : NotesRepositoryComponent =>
  override def getNoteService : NoteService = new DataBaseNoteService

  private class DataBaseNoteService extends NoteService {
    def save(note: Note) : Long = ???

    def query(author: Option[String], hashtag: Option[String]): Future[List[Note]] = ???

    def update(l: Long, note: Note) = ???

    def getAll(): Future[List[Note]] = ???

    def find(id:Long) : Future[Option[Note]] = {
      val fromDatabase: DBIO[Seq[(Long, String, String)]] = getNotesRepository.find(id)
      Future.apply(None)
    }
  }
}







