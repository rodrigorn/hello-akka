package com.grupoabraxas.quicksort

import akka.actor.{Actor, ActorRef, PoisonPill, Props}

class WorkerActor extends Actor {
  import WorkerActor._

  override def receive: Receive = {
    case Sort(numbers) if numbers.length <= 1 =>
      sender ! Sorted(numbers)
    case Sort(numbers) =>
      val pivot = numbers(numbers.length / 2)

      val left = numbers.filter(pivot >)
      val leftWorker = context.actorOf(WorkerActor.props)
      leftWorker ! Sort(left)

      val right = numbers.filter(pivot <)
      val rightWorker = context.actorOf(WorkerActor.props)
      rightWorker ! Sort(right)

      val center = numbers.filter(pivot ==)

      context.become(sortingBoth(sender(), leftWorker, rightWorker, center))

    case _ => unhandled()
  }

  def sortingBoth(parent: ActorRef, leftWorker: ActorRef, rightWorker: ActorRef, center: Array[Int]): Receive = {
    case Sorted(numbers) if sender() == leftWorker => context.become(sortingRight(parent, numbers, center))
    case Sorted(numbers) if sender() == rightWorker => context.become(sortingLeft(parent, numbers, center))
    case _ => unhandled()
  }

  def sortingRight(parent: ActorRef, left: Array[Int], center: Array[Int]): Receive = {
    case Sorted(numbers) => sortedBoth(parent, left, numbers, center)
    case _ => unhandled()
  }

  def sortingLeft(parent: ActorRef, right: Array[Int], center: Array[Int]): Receive = {
    case Sorted(numbers) => sortedBoth(parent, numbers, right, center)
    case _ => unhandled()
  }

  def sortedBoth(parent: ActorRef, left: Array[Int], right: Array[Int], center: Array[Int]) = {
    parent ! Sorted(Array.concat(left, center, right))
    self ! PoisonPill
  }
}

object WorkerActor {

  def props = Props(classOf[WorkerActor])

  case class Sort(numbers: Array[Int])
  case class Sorted(numbers: Array[Int])

}
