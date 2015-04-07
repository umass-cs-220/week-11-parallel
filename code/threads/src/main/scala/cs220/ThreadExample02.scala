package cs220

case class MessageThread(name: String, t: Int) extends Thread {
  val xs = List(
    "Mares eat oats",
    "Does eat oats",
    "Little lambs eat ivy",
    "A kid will eat ivy too"
  )

  override def run = {
    for (x <- xs) {
      Thread.sleep(t)
      println(s"$name: $x")
    }
    println(s"$name: Woohoo! I'm done!")
  }
}

object SleepMessages {
  def messages = {
    println("first thread...")
    MessageThread("T1", 2000).start
    println("second thread...")
    MessageThread("T2", 1000).start
  }

  def messages(c: Int) =
    for (i <- 1 to 10) {
      MessageThread(s"T$i", 400*c).start
    }

  def main(args: Array[String]) = {
    messages
  }
}
