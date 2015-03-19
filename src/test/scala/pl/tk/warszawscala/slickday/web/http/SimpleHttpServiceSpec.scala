package pl.tk.warszawscala.slickday.web.http

import org.specs2.mutable.Specification
import pl.tk.warszawscala.slickday.web.http.model.{Hashtag, Author, MyJsonProtocol, Note}
import pl.tk.warszawscala.slickday.web.service.MockNotesServiceComponent
import spray.http.HttpHeaders.Location
import spray.testkit.Specs2RouteTest
import spray.http._
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport._
import MyJsonProtocol._

/**
 * Created by tomaszk on 3/12/15.
 */

class SimpleHttpServiceSpec extends Specification with Specs2RouteTest with SimpleHttpService with MockNotesServiceComponent {
  def actorRefFactory = system

  val sampleNote = Note(
    None,
    Author("example","example@example.com"),
  List.empty,
  "Content of this note",
  List(Hashtag("#sampleNote"))
  )

  var generatedId = 0

  sequential

    "return empty list of notes" in {
      Get("/rest/notes") ~> serviceRoute ~> check {
        responseAs[List[Note]] must beEmpty
      }
    }

    "be able to serve index.html" in {
      Get("/index.html") ~> serviceRoute ~> check {
        responseAs[String] must not beEmpty
      }
    }

    "save a note" in {
      Post("/rest/notes",sampleNote) ~> serviceRoute ~> check {
        val uriRegex  = """/rest/notes/(\d)""".r
        val locationHeader = header("location")
        val generatedIndex = locationHeader match {
          case Some(Location(Uri(_,_,s,_,_))) => (uriRegex findFirstMatchIn (s.toString())).map(_.group(1))
          case _ => None
        }
        generatedId !== None
        locationHeader === Some(Location(Uri(s"/rest/notes/${generatedIndex.get}")))
      }
    }

    "retrieve that note in all notes" in {
      Get(s"/rest/notes") ~> serviceRoute ~> check {
        responseAs[List[Note]] === List(sampleNote.copy(id = Some(generatedId.toString)))
      }
    }

    "retrieve that note directly" in {
      Get(s"/rest/notes/$generatedId") ~> serviceRoute ~> check {
        responseAs[Note] === sampleNote.copy(id = Some(generatedId.toString))
      }
    }
}