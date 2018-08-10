package com.grupoabraxas.hello

import akka.actor.{Actor, Props}

class GreetingsActor extends Actor {

  import GreetingsActor._

  override def receive: Receive = {
    case Hello(name) => println(s"Hello ${name}!")
    case _ => unhandled()
  }

}

object GreetingsActor {

  def props = Props(classOf[GreetingsActor])

  case class Hello(name: String)
}
