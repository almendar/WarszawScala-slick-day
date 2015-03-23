package pl.tk.warszawscala.slickday.web.http

import akka.actor.Actor
import pl.tk.warszawscala.slickday.repository.SlickLibraryRepositoryComponent
import pl.tk.warszawscala.slickday.service.{MockLibraryServiceComponent, DatabaseLibraryServiceComponent}
import slick.driver.H2Driver

/**
 * Created by tomaszk on 3/12/15.
 */

class LibraryService extends Actor
  with LibraryHttpService
  with DatabaseLibraryServiceComponent
  with SlickLibraryRepositoryComponent
  with H2Driver {
  def actorRefFactory = context
  def receive = runRoute(serviceRoute)
}

