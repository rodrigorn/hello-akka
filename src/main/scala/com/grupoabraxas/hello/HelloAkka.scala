package com.grupoabraxas.hello

import akka.actor.{ActorRef, ActorSystem, PoisonPill}

object HelloAkka extends App {
  import GreetingsActor._

  val system: ActorSystem = ActorSystem.create("hello-akka")
  val actor: ActorRef = system.actorOf(GreetingsActor.props)

  val names = Seq("Akka", "Scala", "Alpakka")
  names.foreach(actor ! Hello(_))

  Thread.sleep(2000)

  actor ! PoisonPill

  system.terminate()
}
