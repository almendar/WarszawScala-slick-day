package pl.tk.warszawscala.slickday.web.repository

import slick.driver.JdbcProfile





/**
 * Create concrete implementation with slick
 */
trait SlickNotesRepositoryComponent  extends NotesRepositoryComponent { self : JdbcProfile =>

  import api._

  type NoteTuple = (Long,String,String)

  private class NotesTable(tag: Tag) extends Table[NoteTuple](tag, "NOTES") {
    def id = column[Long]("ID", O.PrimaryKey)
    def author = column[String]("AUTHOR")
    def content = column[String]("CONTENT")
    def * = (id, author, content)
  }

  private val notesQuery = TableQuery[NotesTable]

  override def getNotesRepository : SlickNotesRepository = new SlickNotesRepository

  class SlickNotesRepository extends NotesRepository {
    def find(id:Long) : DBIO[Seq[NoteTuple]] = notesQuery.filter(_.id === id).result
  }

}


