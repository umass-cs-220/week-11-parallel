/**
 * Akka main application.
 */
package cs220.howdy01

import akka.actor.ActorSystem
import akka.actor.Props

/**
 * This object is the main entry point to our Akka application.
 */
object HowdyAkka extends App {
  // First, create the Akka system:
  val system = ActorSystem("howdy-akka")
  
  // Create a new `Person` actor:
  val alice = system.actorOf(Props(new Person), "alice")
  
  // Send a pint to alice!
  alice ! Pint

  system.shutdown()
}
