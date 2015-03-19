package pl.tk.warszawscala.slickday.web.http

import akka.actor.Actor
import pl.tk.warszawscala.slickday.web.http.model.Note
import pl.tk.warszawscala.slickday.web.repository.SlickNotesRepositoryComponent
import pl.tk.warszawscala.slickday.web.service.{MockNotesServiceComponent, DatabaseNotesServiceComponent}
import slick.driver.H2Driver

/**
 * Created by tomaszk on 3/12/15.
 */

class NoteService extends Actor
  with SimpleHttpService
  with DatabaseNotesServiceComponent
  with SlickNotesRepositoryComponent
  with H2Driver {
  def actorRefFactory = context
  def receive = runRoute(serviceRoute)
}

