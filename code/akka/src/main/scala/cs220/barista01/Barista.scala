package cs220.barista01

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.{ActorRef, Props}

sealed trait CoffeeRequest
case object CappuccinoRequest extends CoffeeRequest
case object EspressoRequest extends CoffeeRequest

case class Bill(cents: Int)
case object ClosingTime

class Barista extends Actor {
  def receive = {
    case CappuccinoRequest =>
      sender ! Bill(250)
      println("I have to prepare a cappuccino!")
    case EspressoRequest =>
      sender ! Bill(200)
      println("Let's prepare an espresso.")
    case ClosingTime =>
      context.system.shutdown
  }
}

object Barista extends App {
  val system = ActorSystem("Barista")

  val barista: ActorRef = system.actorOf(Props[Barista], "Barista")
  val customer = system.actorOf(Props(classOf[Customer], barista), "Customer")

  customer ! CaffeineWithdrawalWarning
  barista ! ClosingTime
}
