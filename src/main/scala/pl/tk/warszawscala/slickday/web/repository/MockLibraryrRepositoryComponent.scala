package pl.tk.warszawscala.slickday.web.repository

import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock

import pl.tk.warszawscala.slickday.web.http.model._

/**
 * Created by tomaszk
 */


trait MockLibraryrRepositoryComponent extends LibraryRepositoryComponent {



  abstract class Repo[A] {
    protected val lock : ReentrantLock = new ReentrantLock()
    var store = List.empty[A]
    protected val idGen = new AtomicLong()

    def persist(a:A) : Long

    def get(id:Long) : Option[A]

    def update(id:Long,a:A) : Unit



  }

  class MockLibraryRepository extends LibraryRepository {

    val authorRepo = new Repo[Author] {
      override def persist(a: Author): Long
      = {
        lock.lock()
        val entity = a.copy(Some(idGen.getAndIncrement))
        store = entity :: store
        lock.unlock()
        entity.id.get
      }

      override def update(id: Long, a: Author): Unit = {
        lock.lock()
        store = a :: store.filterNot(_.id.get == id)
        lock.unlock()
      }

      override def get(id: Long): Option[Author] = store.filter(_.id.get == id).headOption

    }

    val categoryRepo = new Repo[Category] {
      override def persist(a: Category): Long = {
        lock.lock()
        val entity = a.copy(Some(idGen.getAndIncrement))
        store = entity :: store
        lock.unlock()
        entity.id.get
      }

      override def update(id: Long, a: Category): Unit = {
        lock.lock()
        store = a :: store.filterNot(_.id.get == id)
        lock.unlock()
      }

      override def get(id: Long): Option[Category] = store.filter(_.id.get == id).headOption
    }


    val bookRepo = new Repo[Book] {
      override def persist(a: Book): Long = {
        lock.lock()
        val entity = a.copy(Some(idGen.getAndIncrement))
        store = entity :: store
        lock.unlock()
        entity.id.get
      }

      override def update(id: Long, a: Book): Unit = {
        lock.lock()
        store = a :: store.filterNot(_.id.get == id)
        lock.unlock()
      }

      override def get(id: Long): Option[Book] = store.filter(_.id.get == id).headOption
    }
  }
  override val getLibraryRepository: MockLibraryRepository = new MockLibraryRepository
}
