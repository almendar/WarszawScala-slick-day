package pl.tk.warszawscala.slickday.repository

import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock

import pl.tk.warszawscala.slickday.web.http.model._

/**
 * Created by tomaszk
 */


trait MockLibraryRepositoryComponent extends LibraryRepositoryComponent {

  class Repo[A <: IdTransform[A]] {
    import Locking._

    private implicit val lock : ReentrantLock = new ReentrantLock()
    var store : List[A] = List.empty[A]
    private val idGen = new AtomicLong()

    def persist(a:A) : Long = locked {
      val entity: A = a.withNewId(idGen.getAndIncrement)
      store = entity :: store
      entity.getId.get
    }

    def get(id:Long) : Option[A] = {
      store.filter(_.getId.get == id).headOption
    }

    def update(id:Long,a:A) : Unit = locked {
      store = a :: store.filterNot(_.getId.get == id)
    }

    def delete(id:Long) : Unit = locked {
      store = store.filterNot(_.getId == Some(id))
    }
  }

  class CategoryRepo {
    import Locking._

    private implicit val lock : ReentrantLock = new ReentrantLock()
    var store : List[Category] = List.empty[Category]
    private val idGen = new AtomicLong()

    def persist(a: Category): Long = locked {
      val entity = a.withNewId(idGen.getAndIncrement)
      store = entity :: store
      entity.getId.get
    }

    def get(id: Long): List[Category] = {
      store.filter(_.parentId == Some(id)).map{cat =>
        val hasChildren = store.exists(_.parentId == cat.getId)
        if(hasChildren) cat.copy(hasChildren = true) else cat
      }
    }

    def update(id: Long, a: Category): Unit = locked {
      store = a.copy(id = Some(id)) :: store.filterNot(_.getId == Some(id))
    }

    def delete(l:Long) : Unit = {
      def giveChildren(l : Long) : List[Category] = {
        val children: List[Category] = store.filter(_.parentId == Some(l))
        val grandChildren: List[Category] = children.flatMap(c => giveChildren(c.getId.get))
        children ::: grandChildren
      }

      locked {
        val idsToRemove = l :: giveChildren(l).map(_.getId.get)
        store = store.filterNot(c => idsToRemove.contains(c.getId.get))
      }
    }
  }

  class MockLibraryRepository extends LibraryRepository {
    val authorRepo = new Repo[Author]
    val categoryRepo = new CategoryRepo
    val bookRepo = new Repo[Book]
  }

  override val getLibraryRepository: MockLibraryRepository = new MockLibraryRepository

  object Locking {
    def locked[A](f: => A)(implicit lock: ReentrantLock): A = {
      lock.lock()
      val result = f
      lock.unlock()
      result
    }
  }
}