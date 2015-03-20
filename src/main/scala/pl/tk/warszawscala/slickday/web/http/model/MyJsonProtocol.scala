package pl.tk.warszawscala.slickday.web.http.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import spray.json._
import DefaultJsonProtocol._
/**
 * Created by tomaszk on 3/12/15.
 */
object MyJsonProtocol extends DefaultJsonProtocol {


  implicit object LocalDateFormat extends RootJsonFormat[LocalDate] {
    def write(c: LocalDate) = JsNumber(
      c.toEpochDay
     )

    def read(value: JsValue) = value match {
      case JsNumber(value) =>
        LocalDate.ofEpochDay(value.toLongExact)
      case _ => deserializationError("LocalDateTime expected")
    }
  }

  implicit val authorFormat : RootJsonFormat[Author] = jsonFormat2(Author)

  implicit val noteFormat: RootJsonFormat[Category] = rootFormat(lazyFormat(jsonFormat(Category,"id","name","parentCategory")))

  implicit val bookFormat : RootJsonFormat[Book] = jsonFormat5(Book)


}
