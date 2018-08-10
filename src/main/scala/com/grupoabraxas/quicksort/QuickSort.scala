package com.grupoabraxas.quicksort

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.Await
import scala.util.Random

object QuickSort extends App {
  import WorkerActor._

  val system: ActorSystem = ActorSystem.create("hello-akka")
  val actor: ActorRef = system.actorOf(WorkerActor.props)


  val randomNumbers = (for (i <- 1 to 100) yield Random.nextInt(10000)).toArray

  println("Initial numbers:")
  randomNumbers.foreach(println)

  implicit val timeout = new Timeout(500 seconds)
  val future = actor ? Sort(randomNumbers)

  val result = Await.result(future, timeout.duration).asInstanceOf[Sorted]
  println("==============")
  println("Sorted array:")
  result.numbers.foreach(println)

  system.terminate()
}
