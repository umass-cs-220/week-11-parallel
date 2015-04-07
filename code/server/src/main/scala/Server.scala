import java.net._
import java.io._
import scala.io._
import scala.concurrent._

// This is used by the future construct to handle how
// and when asynchronous computation is executed. It is
// kind of like a thread pool.
import ExecutionContext.Implicits.global

/**
 * The 230 Server object.
 */
object Server {

  /**
   * Checks the length of the student id. It should be 8.
   * @param id the student id
   * @return true if it has length 8; false otherwise
   */
  def idsize(id: String) = id.length == 8

  /**
   * Records the correct bonus response in the students id.bonus file.
   * @param id the student's 8-digit spire id
   * @param bonus the bonus answer
   */
  def recordBonus(id: String, bonus: String) {
    val out = new java.io.FileWriter(s"assets/$id.bonus")
    out.write(bonus)
    out.close()
  }

  /**
   * Records a successful answer from the student in the id.succ file.
   * @param id the student's 8-digit spire id
   * @param soln the solution to their problem
   */
  def recordSuccess(id: String, soln: String) {
    val out = new java.io.FileWriter(s"assets/$id.succ")
    out.write(soln)
    out.close()
  }

  /**
   * Records the number of failed attempts in the id.fail file.
   * @param id the student's 8-digit spire id
   */
  def recordFailure(id: String) {
    val file  = new java.io.File(s"assets/$id.fail")
    val fails =
      if (file.exists)
        Source.fromFile(s"assets/$id.fail").getLines().next().toInt + 1
      else 1
    val out = new java.io.FileWriter(file)
    out.write(fails.toString)
    out.close()
  }

  def talk(host: String, port: Int, in: DataInputStream, out: DataOutputStream): Unit = {
    val buf = new Array[Byte](1024)
    var len = 0
    var spireId = ""

    // Phase 1:
    // We receive the Spire ID from the student and we send back the
    // problem associated with their Spire ID. We check to make sure
    // the Spire ID is the proper length and that the associated file
    // exists. If it does then we send back the problem; if it doesn't
    // we send back a "FAILURE" message.
    len = in.read(buf, 0, buf.length)
    if (len != -1) {
      val v = new String(buf.slice(0,len)).trim
      if (idsize(v)) {
        try {
          // Get the problem to send back to student:
          val send = Source.fromFile(s"assets/$v.send").getLines().next()
          out.write(send.getBytes, 0, send.length)
          spireId = v
        } catch {
          case nf: FileNotFoundException =>
            val s = s"FAILURE: unknown spire id '$v'."
            out.write(s.getBytes, 0, s.length)
            return
        }
      }
      else {
        val s = "FAILURE: incorrect spire id size."
        out.write(s.getBytes, 0, s.length)
        return
      }
    }

    // Phase 2:
    // This is the answer phase. We should receive a message from the
    // student's client program that is the solution to the
    // problem. We open up the generated solution to check if it is
    // correct. If it is correct the student was successful and we
    // record their success is a file.
    var solved = false

    len = in.read(buf, 0, buf.length)
    if (len != -1) {
      val v = new String(buf.slice(0, len)).trim
      try {
        // Get the generated solution to compare to students:
        val soln = Source.fromFile(s"assets/$spireId.recv").getLines().next()

        v == soln match {
          case false  =>
            val s = "FAILURE: you got the wrong answer."
            out.write(s.getBytes, 0, s.length)
            recordFailure(spireId)
          case true   =>
            val s = "SUCCESS: you got the correct answer."
            out.write(s.getBytes, 0, s.length)
            recordSuccess(spireId, soln)
            solved = true
        }
      } catch {
        case nf: FileNotFoundException =>
          val s = s"FAILURE: unknown spire id '$v'."
          out.write(s.getBytes, 0, s.length)
          recordFailure(spireId)
      }
    }

    // Phase 3:
    // This is the bonus phase where students can guess the meaning of
    // the port number we are using in this project. If they can guess
    // what it means they get a bonus point. The solution to the bonus
    // is in the bonus.txt file.
    len = in.read(buf, 0, buf.length)
    if (solved && len != -1) {
      val v = new String(buf.slice(0, len)).trim
      // Get the generated solution to compare to students:
      val bonus = Source.fromFile(s"bonus.txt").getLines().next().trim
      v == bonus match {
        case false  =>
          val s = "FAILURE: you did not get the bonus."
          out.write(s.getBytes, 0, s.length)
        case true   =>
          val s = "SUCCESS: you got the bonus answer. Shhh don't tell anyone!"
          out.write(s.getBytes, 0, s.length)
          recordBonus(spireId, v)
      }
    }
  }  

  /**
   * Handles the incoming socket connection.
   * @param sock the connected socket
   */
  def handle(sock: Socket) {
    sock.getRemoteSocketAddress match {
      case addr: InetSocketAddress =>
        val host = addr.getHostString
        val port = addr.getPort
        println(s"$host:$port :: client connected")
        val in  = new DataInputStream(sock.getInputStream)
        val out = new DataOutputStream(sock.getOutputStream)
        talk(host, port, in, out)
        println(s"$host:$port :: disconnecting from client")
      case _ => throw new ClassCastException
    }
  }
  
  /**
   * The main routine.
   * @param args program arguments
   */
  def main(args: Array[String]) {
    if (args.length != 1) {
      println("usage: scala Server port")
      sys.exit(1)
    }
    val port = args(0).toInt

    val server = new ServerSocket(port)
    println(s"230 Server Up ($port)")
    while (true) {
      val sock = server.accept
      // Create a future for handling the connection. We do not care
      // about the return value for the future so we simply ignore it.
      val f: Future[Unit] = Future {
        handle(sock)
      }

      // Check the success or failure of the future:
      f onFailure {
        case t =>
          println("A failure occurred : " + t.getMessage)
          sock.close()
      }

      f onSuccess {
        case _ =>
          println("Client completed successfully.")
          sock.close()
      }
    }
  }

}
