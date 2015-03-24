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

libraryDependencies += "org.specs2" %% "specs2" % "2.4.17"

libraryDependencies +=  "com.typesafe.slick" %% "slick" % "3.0.0-RC1"

libraryDependencies +=  "org.slf4j" % "slf4j-nop" % "1.6.4"

libraryDependencies +=  "com.h2database" % "h2" % "1.3.175"

unmanagedResourceDirectories in Compile <+= (baseDirectory / "static")

//excludeFilter in unmanagedResources := HiddenFileFilter || "project*" || "target*" || "src*"
