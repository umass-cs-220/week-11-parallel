package cs220

class Counter {
  var c = 0

  def inc = c+=1
  def dec = c-=1
  def get = c

  override def toString = c.toString
}

case class Kid(counter: Counter, iterations: Int) extends Thread {
  override def run =
    for (i <- 1 to iterations) {
      counter.inc
    }
}

object CountTheMarbles {
  def main(args: Array[String]) = {
    if (args.length != 2) {
      println("CountTheMarbles #kids #iterations")
      System.exit(1)
    }

    val kids     = args(0).toInt
    val iters    = args(1).toInt
    val marbles  = new Counter
    val kidcount = new Array[Kid](kids)
    for (i <- 0 to kids-1) {
      kidcount(i) = Kid(marbles, iters)
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
