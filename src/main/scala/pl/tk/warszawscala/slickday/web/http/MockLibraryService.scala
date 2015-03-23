package pl.tk.warszawscala.slickday.web.http

import akka.actor.Actor
import pl.tk.warszawscala.slickday.repository.SlickLibraryRepositoryComponent
import pl.tk.warszawscala.slickday.service.{MockLibraryServiceComponent, DatabaseLibraryServiceComponent}
import slick.driver.H2Driver

class MockLibraryService extends Actor
with LibraryHttpService
with MockLibraryServiceComponent
 {
  def actorRefFactory = context
  def receive = runRoute(serviceRoute)
}