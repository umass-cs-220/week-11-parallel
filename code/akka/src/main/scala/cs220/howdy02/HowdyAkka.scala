/**
 * Akka main application.
 */
package cs220.howdy02

import akka.actor.ActorSystem
import akka.actor.Props

/**
 * This object is the main entry point to our Akka application.
 */
object HowdyAkka extends App {
  // First, create the Akka system:
  val system = ActorSystem("howdy-akka")
  
  // Create a new `BarTender` actor:
  val zed = system.actorOf(Props(new BarTender), "zed")
  
  // Create a new `Person` actor:
  val alice = system.actorOf(Props(new Person), "alice")
  
  zed.tell(Ticket, alice)
  
  // This will ensure that we only terminate when a call to shutdown
  // has been called somewhere else.
  system.awaitTermination()
}