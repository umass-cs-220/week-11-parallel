/**
 * This file contains the messages that can be passed amongst Akka actors.
 */
package cs220.howdy03

/**
 * The `FullPint` message represents a full pint of, um, apple juice of course.
 * This version is updated to include a quantity (number of full pints).
 */
case class FullPint(quantity: Int)

/**
 * The `EmptyPint` message represents an empty pint of, um, apple juice of course.
 * This version is updated to include a quantity (number of empty pints).
 */
case class EmptyPint(quanitity: Int)

/**
 * A `Ticket` message represents an order for a pint.
 * This version is updated to include a quantity (number of pints ordered).
 */
case class Ticket(quantity: Int)