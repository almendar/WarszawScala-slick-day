package pl.tk.warszawscala.slickday.web.repository

import slick.driver.JdbcProfile




/**
 * Create concrete implementation with slick
 */
trait NotesRepositoryComponent  { self : JdbcProfile =>

  import api._

  type NoteTuple = (Long,String,String)

  private class NotesTable(tag: Tag) extends Table[NoteTuple](tag, "NOTES") {
    def id = column[Long]("ID", O.PrimaryKey)
    def author = column[String]("AUTHOR")
    def content = column[String]("CONTENT")
    def * = (id, author, content)
  }

  private val notesQuery = TableQuery[NotesTable]

  def getNotesRepository : NotesRepository = new NotesRepository

  class NotesRepository {
    def find(id:Long) : DBIO[Seq[NoteTuple]] = notesQuery.filter(_.id === id).result
  }

}


