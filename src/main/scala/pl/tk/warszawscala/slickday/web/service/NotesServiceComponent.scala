package pl.tk.warszawscala.slickday.web.service

import slick.dbio.DBIO

import scala.concurrent.Future

import pl.tk.warszawscala.slickday.web.http.model.{Hashtag, Author, Note}
import pl.tk.warszawscala.slickday.web.repository.{NotesRepositoryComponent, SlickNotesRepositoryComponent}
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by tomaszk on 3/17/15.
 */
trait NotesServiceComponent { self  : NotesRepositoryComponent =>

  def getNoteService : NoteService

  trait NoteService {
    def save(note: Note) : Future[String]

    def query(author: Option[String], hashtag: Option[String]): Future[List[Note]]

    def update(id: String, note: Note)

    def getAll(): Future[List[Note]]

    def find(id:String) : Future[Option[Note]]
  }
}














