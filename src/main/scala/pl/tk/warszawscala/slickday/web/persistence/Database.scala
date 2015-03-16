package pl.tk.warszawscala.slickday.web.persistence

import Database.AlreadyInDataBaseException
import pl.tk.warszawscala.slickday.web.model.Note

/**
 * Created by tomaszk on 3/12/15.
 */

trait DataBaseProvider[T] {
  def getDataBase : Database[T]
}

object Database {
  class AlreadyInDataBaseException extends Exception
}

trait Database[T] {
  def insert(t:T) : T
  def getAll : List[T]
  def clear() : Unit
}


class NotesDatabase extends Database[Note] {

  private var data : List[Note] = List.empty

  override def insert(inv: Note): Note = {
    if(data.contains(inv)) throw new AlreadyInDataBaseException
    else data = inv :: data
    inv
  }
  override def getAll: List[Note] = data

  override def clear(): Unit = data = List.empty
}


