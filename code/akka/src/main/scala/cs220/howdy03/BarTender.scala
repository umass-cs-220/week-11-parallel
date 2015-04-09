package cs220.howdy03

import akka.actor.Actor
import akka.actor.ActorLogging

class BarTender extends Actor with ActorLogging {
  // The total number of pints served:
  var total = 0
  
  def receive = {
    case Ticket(quantity) =>
      total = total + quantity
      
      log.info(s"I'll get $quantity pints for sender [${sender.path}]")
      
      // Serve up the pints:
      for (number <- 1 to quantity) {
        log.info(s"Pint $number is coming up for [${sender.path}]")
        Thread.sleep(1000)
        log.info(s"Pint $number is ready for [${sender.path}]")
        sender ! FullPint(number)
      }
      
    case EmptyPint(number) =>
      total match {
        case 1 =>
          log.info("You all drank that fast, time to close shop")
          context.system.shutdown()
        case n =>
          total = total - 1
          log.info(s"You drank pint $number fast!")
          log.info(s"There are still $total pints left.")
      }
  }
}