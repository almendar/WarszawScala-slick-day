package pl.tk.warszawscala.slickday.web.http

import akka.actor.Actor
import pl.tk.warszawscala.slickday.web.model.Note
import pl.tk.warszawscala.slickday.web.persistence.{NotesDatabase, Database, DataBaseProvider}

/**
 * Created by tomaszk on 3/12/15.
 */

class InvitationService extends Actor with SimpleHttpService with DataBaseProvider[Note] {
  def actorRefFactory = context
  def receive = runRoute(serviceRoute)
  override val getDataBase: Database[Note] = new NotesDatabase
}
