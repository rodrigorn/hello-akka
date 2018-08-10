import sbt._

object Dependencies {
  lazy val akkaVersion = "2.5.14"

  lazy val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion
}
