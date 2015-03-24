package pl.tk.warszawscala.slickday.repository

import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock

import pl.tk.warszawscala.slickday.web.http.model._

/**
 * Created by tomaszk
 */


trait MockLibraryRepositoryComponent extends LibraryRepositoryComponent {

  class Repo[A <: IdTransform[A]] {

    private val lock : ReentrantLock = new ReentrantLock()
    var store : List[A] = List.empty[A]
    private val idGen = new AtomicLong()

    def persist(a:A) : Long = {
      lock.lock()
      val entity: A = a.withNewId(idGen.getAndIncrement)
      store = entity :: store
      lock.unlock()
      entity.getId.get
    }

    def get(id:Long) : Option[A] = {
      store.filter(_.getId.get == id).headOption
    }

    def update(id:Long,a:A) : Unit = {
      lock.lock()
      store = a :: store.filterNot(_.getId.get == id)
      lock.unlock()
    }

    def delete(id:Long) : Unit = {
      lock.lock()
      store = store.filterNot(_.getId == Some(id))
      lock.unlock()
    }
  }

  class MockLibraryRepository extends LibraryRepository {
    val authorRepo = new Repo[Author]
    val categoryRepo = new  {

      private val lock : ReentrantLock = new ReentrantLock()
      var store : List[Category] = List.empty[Category]
      private val idGen = new AtomicLong()

       def persist(a: Category): Long = {
         lock.lock()
         val newId = idGen.incrementAndGet();
         store = a.copy(id = Some(newId)) :: store
         lock.unlock()
         newId
       }

       def get(id: Long): List[Category] = {
        store.filter(_.parentId == Some(id)).map{cat =>
          val hasChildren = store.exists(_.parentId == cat.getId)
          if(hasChildren) cat.copy(hasChildren = true) else cat
        }
      }

      def update(id: Long, a: Category): Unit = {
        lock.lock
        store = a.copy(id = Some(id)) :: store.filterNot(_.getId == Some(id))
        lock.unlock()
      }

      def delete(l:Long) : Unit = {
        def giveChildren(l : Long) : List[Category] = {
          val children: List[Category] = store.filter(_.parentId == Some(l))
          val grandChildren: List[Category] = children.flatMap(c => giveChildren(c.getId.get))
          children ::: grandChildren
        }
        lock.lock()
        val idsToRemove = l :: giveChildren(l).map(_.getId.get)
        store = store.filterNot(c => idsToRemove.contains(c.getId.get))
        lock.unlock()
      }
    }
    val bookRepo = new Repo[Book]
  }
  override val getLibraryRepository: MockLibraryRepository = new MockLibraryRepository
}
