package pl.tk.warszawscala.slickday.web.http

import akka.actor.Actor
import pl.tk.warszawscala.slickday.web.http.model.{Hashtag, Author, Note}
import pl.tk.warszawscala.slickday.web.repository.SlickNotesRepositoryComponent
import pl.tk.warszawscala.slickday.web.service.{MockNotesServiceComponent, DatabaseNotesServiceComponent}
import slick.driver.H2Driver

class MockNoteService extends Actor
with SimpleHttpService
with MockNotesServiceComponent
 {
  val sampleNote = Note(
    None,
    Author("example","example@example.com"),
    List.empty,
    "Content of this note",
    List(Hashtag("#sampleNote"))
  )
  getNoteService.save(sampleNote)
  def actorRefFactory = context
  def receive = runRoute(serviceRoute)
}