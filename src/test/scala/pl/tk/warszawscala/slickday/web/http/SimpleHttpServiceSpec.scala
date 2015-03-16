package pl.tk.warszawscala.slickday.web.http

import org.specs2.mutable.Specification
import pl.tk.evojam.web.persistence.InvitationsDatabase
import pl.tk.warszawscala.slickday.web.model.{JsonProtocol, Note}
import pl.tk.warszawscala.slickday.web.persistence.{NotesDatabase, Database, DataBaseProvider}
import spray.testkit.Specs2RouteTest
import spray.http._
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport._
import JsonProtocol._

/**
 * Created by tomaszk on 3/12/15.
 */

class SimpleHttpServiceSpec extends Specification with Specs2RouteTest with SimpleHttpService with DataBaseProvider[Note] {
  def actorRefFactory = system
  val db = new NotesDatabase
  override def getDataBase: Database[Note] = db
  val sampleInvitation = null//Note("John Smith","john@smith.mx",)
  val wrongEntity = null//Note("John Smith","")

  sequential

  "The SimpleHttpService" should {
    "return a empty list of invitess on start with GET request to /invitation" in {
      Get("/invitation") ~> serviceRoute ~> check { responseAs[List[Note]] must beEmpty }
    }
    "return code 201 when created an Entity" in {
      Post("/invitation", sampleInvitation) ~> serviceRoute ~> check { status === StatusCodes.Created }
    }
    "return arrays of added invitations when GET request to /invitation" in {
      Get("/invitation") ~> serviceRoute ~> check { responseAs[List[Note]] shouldEqual List(sampleInvitation) }
    }
    "return code 422 when trying to add exisiting Entity" in {
      Post("/invitation", sampleInvitation) ~> serviceRoute ~> check {
        status === StatusCodes.UnprocessableEntity
      }
    }
    "reject creation of wrong entity" in {
        Post("/invitation", wrongEntity) ~> sealRoute(serviceRoute) ~> check {
          status === StatusCodes.BadRequest
        }
      }
    }
}