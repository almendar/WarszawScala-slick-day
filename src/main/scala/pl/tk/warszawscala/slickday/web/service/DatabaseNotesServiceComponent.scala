package pl.tk.warszawscala.slickday.web.service

import pl.tk.warszawscala.slickday.web.http.model.Note
import pl.tk.warszawscala.slickday.web.repository.SlickNotesRepositoryComponent
import slick.dbio._

import scala.concurrent.Future

trait DatabaseNotesServiceComponent extends NotesServiceComponent { self : SlickNotesRepositoryComponent =>
  override def getNoteService : NoteService = new DataBaseNoteService

  private class DataBaseNoteService extends NoteService {
    def save(note: Note) : Future[String] = ???

    def query(author: Option[String], hashtag: Option[String]): Future[List[Note]] = ???

    override def update(id: String, note: Note): Unit = ???

    def getAll(): Future[List[Note]] = ???

    def find(id: String): Future[Option[Note]] = {
      val fromDatabase: DBIO[Seq[(Long, String, String)]] = getNotesRepository.find(id.toLong)
      Future.successful(None)
    }




  }
}