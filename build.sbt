name := "Slick-Keep"

version := "1.0.0"

scalaVersion := "2.11.2"

Revolver.settings

val sprayV ="1.3.2"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.6"

libraryDependencies += "io.spray" %% "spray-routing" % sprayV

libraryDependencies += "io.spray" %% "spray-can" % sprayV

libraryDependencies += "io.spray" %%  "spray-json" % "1.3.1"

libraryDependencies +=  "io.spray" %% "spray-testkit" % sprayV % "test"

libraryDependencies +=  "com.typesafe.slick" %% "slick" % "3.0.0-RC1"

libraryDependencies +=  "org.slf4j" % "slf4j-nop" % "1.6.4"