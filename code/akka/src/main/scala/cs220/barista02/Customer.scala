package cs220.barista02

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.{ActorRef, Props}

case object CaffeineWithdrawalWarning

class Customer(caffeineSource: ActorRef) extends Actor {
  def receive = {
    case CaffeineWithdrawalWarning =>
      caffeineSource ! EspressoRequest
    case Bill(cents) =>
      println(s"I have to pay $cents, or else!")
  }
}
