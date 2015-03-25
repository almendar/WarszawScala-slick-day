package pl.tk.warszawscala.slickday.repository

import org.specs2._
import pl.tk.warszawscala.slickday.service.MockLibraryServiceComponent
import pl.tk.warszawscala.slickday.web.http.model.Category

/**
 * Created by tomaszk on 3/25/15.
 */


class MockLibraryRepositorySpec extends Specification with MockLibraryServiceComponent  {

  implicit class FutureUnpack[A](value : scala.concurrent.Future[A]) {
    def futureValue : A = value.value.get.get
  }


    val rc1 = Category(None, "root-category1", None, true)
    val rc2 = Category(None, "root-category2", None, false)
    val cc1 = Category(None, "child-1level-category1", None, true)
    val cc12 = Category(None, "child-2level-category1", None, false)

    val rc1WithId = rc1.copy(id = Some(getLibraryService.save(rc1.copy(hasChildren = false)).value.get.get))
    val rc2WithId = rc2.copy(id = Some(getLibraryService.save(rc2.copy(hasChildren = false)).value.get.get))
    val cc1WithId = {
      val cc1WithParent = cc1.copy(parentId = rc1WithId.id)
      val cc1Id = getLibraryService.save(cc1WithParent.copy(hasChildren = false)).futureValue
      cc1WithParent.copy(id = Some(cc1Id))
    }
    val cc12WithId = {
      val cc12WithParent = cc12.copy(parentId = cc1WithId.id)
      val cc12Id = getLibraryService.save(cc12WithParent.copy(hasChildren = false)).futureValue
      cc12WithParent.copy(id = Some(cc12Id))
    }

  def is = sequential ^  s2"""

 This is a specification to check if categories are persisted in service mock correclty

 The 'Category persistance' should
   return all root categories                                    $e1
   return child of root category                                 $e2
   return child of a non-root category                           $e3
   on delete of parent category child should also extinct        $e4
                                                                 """

  def e1 = {
    getLibraryService.getAllCategories().value.get.get.toSet must beEqualTo(List(rc1WithId,rc2WithId).toSet)
  }

  def e2 = {
    rc1WithId.getId.map {id => getLibraryService.findCategoryById(id)}.get.value.get.get must beEqualTo(List(cc1WithId))
  }

  def e3 = {
    cc1WithId.getId.map {id => getLibraryService.findCategoryById(id)}.get.futureValue must beEqualTo(List(cc12WithId))
  }
  def e4 = {
    getLibraryService.deleteCategory(rc1WithId.getId.get)
    (getLibraryService.getAllCategories().value.get.get must beEqualTo(List(rc2WithId))) and
    (getLibraryService.findCategoryById(rc1WithId.id.get).futureValue must beEmpty) and
    (getLibraryService.findCategoryById(cc1WithId.id.get).futureValue must beEmpty)
  }
}
