/**
 * This file contains the messages that can be passed amongst Akka actors.
 */
package cs220.howdy02

/**
 * The `FullPint` message represents a full pint of, um, apple juice of course.
 */
case object FullPint

/**
 * The `EmptyPint` message represents an empty pint of, um, apple juice of course.
 */
case object EmptyPint

/**
 * A `Ticket` message represents an order for a pint.
 */
case object Ticket