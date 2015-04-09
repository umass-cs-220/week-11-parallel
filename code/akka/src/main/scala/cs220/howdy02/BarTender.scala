package cs220.howdy02

import akka.actor.Actor
import akka.actor.ActorLogging

class BarTender extends Actor with ActorLogging {
  def receive = {
    case Ticket =>
      log.info("1 pint coming right up!")
      Thread.sleep(1000)
      sender ! FullPint
    case EmptyPint =>
      log.info("I think you might be done for the day")
      context.system.shutdown()
  }
}