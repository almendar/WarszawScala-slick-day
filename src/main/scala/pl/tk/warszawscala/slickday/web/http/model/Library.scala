package pl.tk.warszawscala.slickday.web.http.model

import java.time.{LocalDate, LocalDateTime}

trait IdTransform[+A] {
  def withNewId(id:Long) : A
  def getId : Option[Long]
}
case class Author(id:Option[Long], name:String) extends IdTransform[Author] {
  override def withNewId(id: Long): Author = copy(id = Some(id))

  override def getId: Option[Long] = id
}
case class Book(id:Option[Long],authors:List[Author],title:String, category : Category, publishDate:LocalDate) extends IdTransform[Book] {
  override def withNewId(id: Long): Book = copy(id = Some(id))

  override def getId: Option[Long] = id
}
case class Category(id:Option[Long],name:String, parentId:Option[Long], hasChildren:Boolean) extends IdTransform[Category] {
  override def withNewId(id: Long): Category = copy(id = Some(id))
  override def getId: Option[Long] = id
}
