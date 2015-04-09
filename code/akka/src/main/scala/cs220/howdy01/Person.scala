package cs220.howdy01

import akka.actor.Actor
import akka.actor.ActorLogging

/**
 * The `Person` actor represents a person who can receive pints.
 */
class Person extends Actor with ActorLogging {
  // Every actor must define the `receive` method. The `receive` method
  // handles incoming messages. It expects an anonymous function with a
  // "single" argument that is the message received. In Scala, you can
  // pattern match directly on the single argument by writing case
  // statements.
  def receive = {
    case Pint => log.info("Thanks for the pint!")
  }
}