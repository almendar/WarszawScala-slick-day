package pl.tk.warszawscala.slickday.repository

import slick.driver.JdbcProfile

/**
 * Create concrete implementation with slick
 */
trait SlickLibraryRepositoryComponent  extends LibraryRepositoryComponent { self : JdbcProfile =>

  import api._


  override def getLibraryRepository : SlickLibraryRepository = new SlickLibraryRepository

  class SlickLibraryRepository extends LibraryRepository

}


