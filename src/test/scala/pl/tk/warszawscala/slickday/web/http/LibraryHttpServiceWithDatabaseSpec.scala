package pl.tk.warszawscala.slickday.web.http

import pl.tk.warszawscala.slickday.repository.SlickLibraryRepositoryComponent
import pl.tk.warszawscala.slickday.service.{DatabaseLibraryServiceComponent, MockLibraryServiceComponent}
import slick.driver.H2Driver

/**
 * Created by tomaszk
 */
class LibraryHttpServiceWithDatabaseSpec extends LibraryHttpServiceSpec with DatabaseLibraryServiceComponent
with SlickLibraryRepositoryComponent with H2Driver
