package cs220

case class HelloThread(s: String) extends Thread {
  override def run = println(s"Hello from a thread: $s!")
}

object ThreadExample {
  def thread(s: String) = HelloThread(s)

  def main(args: Array[String]) = {
    thread("Hazzah!").start
  }
}
