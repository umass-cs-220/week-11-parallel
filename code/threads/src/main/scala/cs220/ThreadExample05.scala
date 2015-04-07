package cs220

import java.util.concurrent.atomic._

class AtomicCounter {
  var c: AtomicInteger = new AtomicInteger

  def inc = c.getAndIncrement
  def dec = c.getAndDecrement
  def get = c.get

  override def toString = c.toString
}

case class SmartestKid(counter: AtomicCounter, iterations: Int) extends Thread {
  override def run =
    for (i <- 1 to iterations) {
      counter.inc
    }
}

object CountTheMarblesBest {
  def main(args: Array[String]) = {
    if (args.length != 2) {
      println("CountTheMarblesBest #kids #iterations")
      System.exit(1)
    }

    val kids     = args(0).toInt
    val iters    = args(1).toInt
    val marbles  = new AtomicCounter
    val kidcount = new Array[SmartestKid](kids)
    for (i <- 0 to kids-1) {
      kidcount(i) = SmartestKid(marbles, iters)
    }

    for (i <- 0 to kids-1) {
      kidcount(i).start
    }

    for (i <- 0 to kids-1) {
      kidcount(i).join
    }

    // The total number of marbels should be the number of iterations times the
    // number of kids counting the marbles...
    val total = iters*kids

    println(s"Number of marbles counted by ${kids} kid(s) is ${marbles.get}")
    if (total != marbles.get)
      println(s"  Yikes! It should have been $total!")
  }
}
