package pl.tk.warszawscala.slickday

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import com.typesafe.config.{ConfigFactory, Config}
import pl.tk.warszawscala.slickday.web.http.InvitationService
import spray.can.Http

object Boot extends App {

  val config = ConfigFactory.load()
  val interface = config.getString("app.interface")
  val port = config.getInt("app.port")
  implicit val system = ActorSystem("on-spray-can")
  val service = system.actorOf(Props[InvitationService], "invitation-service")
  IO(Http) ! Http.Bind(service, interface, port = port)
}