package pl.tk.warszawscala.slickday.web.http

import akka.actor.Actor
import pl.tk.warszawscala.slickday.web.repository.SlickLibraryRepositoryComponent
import pl.tk.warszawscala.slickday.web.service.{MockLibraryServiceComponent, DatabaseNotesServiceComponent}
import slick.driver.H2Driver

/**
 * Created by tomaszk on 3/12/15.
 */

class LibraryService extends Actor
  with SimpleHttpService
  with DatabaseNotesServiceComponent
  with SlickLibraryRepositoryComponent
  with H2Driver {
  def actorRefFactory = context
  def receive = runRoute(serviceRoute)
}

