package pl.tk.warszawscala.slickday.web.http

import akka.actor.Actor
import pl.tk.warszawscala.slickday.web.repository.SlickLibraryRepositoryComponent
import pl.tk.warszawscala.slickday.web.service.{MockLibraryServiceComponent, DatabaseNotesServiceComponent}
import slick.driver.H2Driver

class MockLibraryService extends Actor
with SimpleHttpService
with MockLibraryServiceComponent
 {
  def actorRefFactory = context
  def receive = runRoute(serviceRoute)
}