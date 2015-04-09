/**
 * Akka main application.
 */
package cs220.howdy03

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
  
  // Create a new `Person` actors:
  val alice   = system.actorOf(Props(new Person), "alice")
  val bob     = system.actorOf(Props(new Person), "bob")
  val charlie = system.actorOf(Props(new Person), "charlie")
  
  // Tell zed the orders:
  zed.tell(Ticket(2), alice)
  zed.tell(Ticket(3), bob)
  zed.tell(Ticket(1), charlie)
  
  // This will ensure that we only terminate when a call to shutdown
  // has been called somewhere else.
  system.awaitTermination()
}