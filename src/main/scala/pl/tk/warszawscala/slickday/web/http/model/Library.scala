package pl.tk.warszawscala.slickday.web.http.model

import java.time.{LocalDate, LocalDateTime}

/**
 * Created by tomaszk
 */
case class Author(id:Option[Long], name:String)
case class Book(id:Option[Long],authors:List[Author],title:String, category : Category, publishDate:LocalDate)
case class Category(id:Option[Long],name:String,parentCategory:Option[Category])

