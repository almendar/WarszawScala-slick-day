package pl.tk.warszawscala.slickday.web.http.model

import java.time.LocalDateTime

case class Note(id:Option[String], owner:Author,coAuthors:List[Author],
                content:String, hashtags:List[Hashtag],
                val created:LocalDateTime = LocalDateTime.now(),
                lastModified:LocalDateTime = LocalDateTime.now())
