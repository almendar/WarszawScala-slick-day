package pl.tk.warszawscala.slickday.web.http

import java.time.LocalDate

import org.specs2.mutable.Specification
import pl.tk.warszawscala.slickday.web.http.model._
import pl.tk.warszawscala.slickday.service.{LibraryServiceComponent, MockLibraryServiceComponent}
import spray.http.HttpHeaders.Location
import spray.testkit.Specs2RouteTest
import spray.http._
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport._
import MyJsonProtocol._


trait LibraryHttpServiceSpec extends Specification with Specs2RouteTest with LibraryHttpService {
  self: LibraryServiceComponent =>

  def actorRefFactory = system

  val AUTHORS_ENDPOINT: String = "/rest/authors"
  val BOOKS_ENDPOINT: String = "/rest/books"
  val CATEGORIES_ENDPOINT: String = "/rest/categories"

  var sampleAuthor = Author(None, "F. Scott Fitzgerald")

  var sampleCategory1 = Category(None, "Great American Novel", None,false)

  var sampleCategory2 = Category(None, "Rich people", None, false)

  var sampleCategory3 = Category(None, "Poor people", None, false)

  var sampleBook: Book = Book(None, List.empty, "The Great Gatsby", sampleCategory2, LocalDate.of(1925, 10, 22))


  sequential

  "be able to serve index.html" in {
    Get("/index.html") ~> serviceRoute ~> check {
      responseAs[String] must not beEmpty
    }
  }


  "return empty list of books" in {
    Get(BOOKS_ENDPOINT) ~> serviceRoute ~> check {
      responseAs[List[Book]] must beEmpty
    }
  }

  "return empty list of authors" in {
    Get(AUTHORS_ENDPOINT) ~> serviceRoute ~> check {
      responseAs[List[Author]] must beEmpty
    }
  }

  "return empty list of categories" in {
    Get(CATEGORIES_ENDPOINT) ~> serviceRoute ~> check {
      responseAs[List[Category]] must beEmpty
    }
  }

  "save an author" in {
    Post(AUTHORS_ENDPOINT, sampleAuthor) ~> serviceRoute ~> check {
      val uriRegex = s"""${AUTHORS_ENDPOINT}/(\\d+)""".r
      val locationHeader = header("location")
      val generatedIndex: Option[String] = locationHeader match {
        case Some(Location(Uri(_, _, s, _, _))) => (uriRegex findFirstMatchIn s.toString).map(_.group(1))
        case _ => None
      }
      sampleAuthor = sampleAuthor.copy(id = generatedIndex.map(_.toLong))
      locationHeader === Some(Location(Uri(s"$AUTHORS_ENDPOINT/${generatedIndex.get}")))

    }
  }

  "save a categories" in {
    Post(CATEGORIES_ENDPOINT, sampleCategory1) ~> serviceRoute ~> check {
      val uriRegex = s"""${CATEGORIES_ENDPOINT}/(\\d+)""".r
      val locationHeader = header("location")
      val generatedIndex: Option[String] = locationHeader match {
        case Some(Location(Uri(_, _, s, _, _))) => (uriRegex findFirstMatchIn s.toString).map(_.group(1))
        case _ => None
      }
      sampleCategory1 = sampleCategory1.copy(id = generatedIndex.map(_.toLong))
      locationHeader === Some(Location(Uri(s"$CATEGORIES_ENDPOINT/${generatedIndex.get}")))
    }

    sampleCategory2 = sampleCategory2.copy(parentId = sampleCategory1.getId)
    Post(CATEGORIES_ENDPOINT, sampleCategory2) ~> serviceRoute ~> check {
      val uriRegex = s"""${CATEGORIES_ENDPOINT}/(\\d+)""".r
      val locationHeader = header("location")
      val generatedIndex: Option[String] = locationHeader match {
        case Some(Location(Uri(_, _, s, _, _))) => (uriRegex findFirstMatchIn s.toString).map(_.group(1))
        case _ => None
      }
      sampleCategory2 = sampleCategory2.copy(id = generatedIndex.map(_.toLong))
      locationHeader === Some(Location(Uri(s"$CATEGORIES_ENDPOINT/${generatedIndex.get}")))
    }

    sampleCategory3 = sampleCategory3.copy(parentId = sampleCategory1.getId)
    Post(CATEGORIES_ENDPOINT, sampleCategory3) ~> serviceRoute ~> check {
      val uriRegex = s"""${CATEGORIES_ENDPOINT}/(\\d+)""".r
      val locationHeader = header("location")
      val generatedIndex: Option[String] = locationHeader match {
        case Some(Location(Uri(_, _, s, _, _))) => (uriRegex findFirstMatchIn s.toString).map(_.group(1))
        case _ => None
      }
      sampleCategory3 = sampleCategory3.copy(id = generatedIndex.map(_.toLong))
      locationHeader === Some(Location(Uri(s"$CATEGORIES_ENDPOINT/${generatedIndex.get}")))
    }
  }


  "save a book" in {
    sampleBook = sampleBook.copy(authors = List(sampleAuthor), category = sampleCategory2)
    Post(BOOKS_ENDPOINT, sampleBook) ~> serviceRoute ~> check {
      val uriRegex = s"""${BOOKS_ENDPOINT}/(\\d+)""".r
      val locationHeader = header("location")
      val generatedIndex: Option[String] = locationHeader match {
        case Some(Location(Uri(_, _, s, _, _))) => (uriRegex findFirstMatchIn s.toString).map(_.group(1))
        case _ => None
      }
      sampleBook = sampleBook.copy(id = generatedIndex.map(_.toLong))
      locationHeader === Some(Location(Uri(s"$BOOKS_ENDPOINT/${generatedIndex.get}")))
    }
  }

  "retrieve author directly" in {
    Get(s"$AUTHORS_ENDPOINT/${sampleAuthor.id.get}") ~> serviceRoute ~> check {
      println("Author:")
      println(responseAs[String])
      println("*" * 20)
      responseAs[Author] === sampleAuthor
    }
  }

  "retrieve parent category children directly" in {
    Get(s"$CATEGORIES_ENDPOINT/${sampleCategory1.id.get}") ~> serviceRoute ~> check {
      println(s"Category children of id:${sampleCategory1.id.get}")
      println(responseAs[String])
      println("*" * 20)
      responseAs[List[Category]] contains sampleCategory2
      responseAs[List[Category]] contains sampleCategory3
    }
  }

  "parent category should have children set to true" in {
    Get(s"$CATEGORIES_ENDPOINT") ~> serviceRoute ~> check {
      responseAs[List[Category]] contains sampleCategory1.copy(hasChildren = true)
    }
  }


  "retrieve that book in all books" in {
    Get(BOOKS_ENDPOINT) ~> serviceRoute ~> check {
      responseAs[List[Book]] must contain(sampleBook)
    }
  }

  "retrieve that book directly" in {
    Get(s"$BOOKS_ENDPOINT/${sampleBook.id.get}") ~> serviceRoute ~> check {
      println("Book:")
      println(responseAs[String])
      println("*" * 20)
      responseAs[Book] === sampleBook
    }
  }

  "delete book directly" in {

    Delete(s"$BOOKS_ENDPOINT/${sampleBook.id.get}") ~> serviceRoute ~> check {
      status mustEqual StatusCode.int2StatusCode(204)
    }

    Get(BOOKS_ENDPOINT) ~> serviceRoute ~> check {
      responseAs[List[Book]] must beEmpty
    }
  }

  "delete author directly" in {

    Delete(s"$AUTHORS_ENDPOINT/${sampleAuthor.id.get}") ~> serviceRoute ~> check {
      status mustEqual StatusCode.int2StatusCode(204)
    }

    Get(AUTHORS_ENDPOINT) ~> serviceRoute ~> check {
      responseAs[List[Author]] must beEmpty
    }
  }

  "delete sub category" in {
    Delete(s"$CATEGORIES_ENDPOINT/${sampleCategory2.id.get}") ~> serviceRoute ~> check {
      status mustEqual StatusCode.int2StatusCode(204)
    }

    Get(s"$CATEGORIES_ENDPOINT/${sampleCategory1.id.get}") ~> serviceRoute ~> check {
      responseAs[List[Category]] must contain(sampleCategory3)
    }

  }

  "delete all categories" in {
    Delete(s"$CATEGORIES_ENDPOINT/${sampleCategory1.id.get}") ~> serviceRoute ~> check {
      status mustEqual StatusCode.int2StatusCode(204)
    }

    Get(s"$CATEGORIES_ENDPOINT") ~> serviceRoute ~> check {
      responseAs[List[Category]] must beEmpty
    }

  }



}
