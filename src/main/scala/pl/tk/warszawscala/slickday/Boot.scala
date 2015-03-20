package pl.tk.warszawscala.slickday

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import com.typesafe.config.{ConfigFactory, Config}
import pl.tk.warszawscala.slickday.web.http.{MockLibraryService, LibraryService}
import spray.can.Http

object Boot extends App {

  val config = ConfigFactory.load()
  val interface = config.getString("app.interface")
  val port = config.getInt("app.port")
  val runWithMock = config.getBoolean("app.runWithMock")
  implicit val system = ActorSystem("notes-system")
  val service =
    if(runWithMock)
      system.actorOf(Props[MockLibraryService],"notes-service")
    else
      system.actorOf(Props[LibraryService], "notes-service")
  IO(Http) ! Http.Bind(service, interface, port = port)
}