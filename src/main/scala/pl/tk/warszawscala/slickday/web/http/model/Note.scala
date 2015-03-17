package pl.tk.warszawscala.slickday.web.http.model

import java.time.LocalDateTime

case class Note(owner:Author,coAuthors:Option[List[Author]],
                content:String, hashtags:List[Hashtag],
                created:LocalDateTime,
                lastModified:LocalDateTime)
