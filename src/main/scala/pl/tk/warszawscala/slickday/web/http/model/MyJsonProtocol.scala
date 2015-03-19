package pl.tk.warszawscala.slickday.web.http.model

import java.time.{ZoneOffset, LocalDateTime}

import spray.json._
import DefaultJsonProtocol._
/**
 * Created by tomaszk on 3/12/15.
 */
object MyJsonProtocol extends DefaultJsonProtocol {


  implicit object LocalDateTimeFormat extends RootJsonFormat[LocalDateTime] {
    def write(c: LocalDateTime) =
      JsArray(JsNumber(c.toInstant(ZoneOffset.UTC).getEpochSecond),
        JsNumber(c.toInstant(ZoneOffset.UTC).getNano))

    def read(value: JsValue) = value match {
      case JsArray(Vector(JsNumber(value),JsNumber(value1))) =>
        LocalDateTime.ofEpochSecond(value.toLongExact,value1.toIntExact,ZoneOffset.UTC)
      case _ => deserializationError("LocalDateTime expected")
    }
  }

  implicit val authorFormat : RootJsonFormat[Author] = jsonFormat2(Author)
  implicit val hashTagFormat : RootJsonFormat[Hashtag] = jsonFormat2(Hashtag)
  implicit val noteFormat: RootJsonFormat[Note] = jsonFormat7(Note)
}
