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
  }

  class MockLibraryRepository extends LibraryRepository {
    val authorRepo = new Repo[Author]
    val categoryRepo = new Repo[Category]
    val bookRepo = new Repo[Book]
  }
  override val getLibraryRepository: MockLibraryRepository = new MockLibraryRepository
}
