package cs220.philosophers.messages

import scala.math._

object Side extends Enumeration {
  // Do not worry about what this means at the moment...
  type Side = Value
  
  // Each receive a value that is unique:
  val Left  = Value
  val Right = Value
  
  // Get the left or right side:
  def randomSide = Side(floor(Side.values.size * random).intValue)
  
  // Get the other one:
  def otherSide(side: Side.Value) = Side.values.find(_ != side).get
}