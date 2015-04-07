package cs220

class SynchronizedCounter {
  var c = 0

  def inc = {
    this.synchronized {
      c+=1
    }
  }

  def dec = {
    this.synchronized {
      c-=1
    }
  }
  
  def get = c

  override def toString = c.toString
}

case class SmarterKid(counter: SynchronizedCounter, iterations: Int) extends Thread {
  override def run =
    for (i <- 1 to iterations) {
      counter.inc
    }
}

object CountTheMarblesBetter {
  def main(args: Array[String]) = {
    if (args.length != 2) {
      println("CountTheMarblesBetter #kids #iterations")
      System.exit(1)
    }

    val kids     = args(0).toInt
    val iters    = args(1).toInt
    val marbles  = new SynchronizedCounter
    val kidcount = new Array[SmarterKid](kids)
    for (i <- 0 to kids-1) {
      kidcount(i) = SmarterKid(marbles, iters)
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
