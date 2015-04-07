import java.io._
import scala.io._
import scala.util._

/** The ProblemGenerator generates problems to be served by the server
  * part of this application. Given a list of student 8-digit spire
  * IDs this object will generate an assets directory containing the
  * problems to be sent to student programs and their solutions.
  *
  * SPIREID.send : This file contains the problem that is sent to the
  * student program. These problems are automatically generated for
  * each student.
  *
  * SPIREID.recv : This file contains the solution to the
  * automatically generated problem. This is used to check against the
  * solution sent by the student program.
  */
object ProblemGenerator {

  /** This represents a generated problem.
    */
  case class Problem(send: String, recv: String)

  /** rand returns a random integer between `from` and `to`.
    */
  def rand(from: Int, to: Int, seed: Int): Int = {
    val r = new Random(seed)
    from + r.nextInt((to - from) + 1)
  }

  ////////////////////////////////////////////////////////////////
  // The methods below solve the various problems generated for //
  // students.                                                  //
  ////////////////////////////////////////////////////////////////

  /** fib generates the solution to the nth fibonacci number.
    */
  def fib(n: Int, nxt: Int, res: Int): Int = {
    n match {
      case 0 => res
      case _ => fib(n-1, nxt+res, nxt)
    }
  }

  /** fac generates the factorial of n.
    */
  def fac(n: Int): Int = {
    def fac2(acc: Int, n: Int): Int =
      n match {
        case 0 => acc
        case _ => fac2(n*acc, n-1)
      }
    fac2(1, n)
  }

  /** max determines the max of x and y.
    */
  def max(x: Int, y: Int): Int = 
    if (x > y) x else y

  /** min determines the min of x and y.
    */
  def min(x: Int, y: Int): Int = 
    if (x < y) x else y

  /** fibGen generates the fibonacci problem for n.
    */
  def fibGen(n: Int): Problem = {
    val v = fib(n, 1, 0)
    new Problem(s"FIB $n", s"$v")
  }

  /** facGen generates the factorial problem for n.
    */
  def facGen(n: Int): Problem = {
    val v = fac(n)
    new Problem(s"FAC $n", s"$v")
  }

  /** maxGen generates the max problem for x and y.
    */
  def maxGen(x: Int, y: Int): Problem = {
    val v = max(x, y)
    new Problem(s"MAX $x $y", s"$v")
  }

  /** minGen generates the min problem for x and y.
    */
  def minGen(x: Int, y: Int): Problem = {
    val v = min(x, y)
    new Problem(s"MIN $x $y", s"$v")
  }

  /** sumGen generates the sum problem for x and y.
    */
  def sumGen(x: Int, y: Int): Problem = {
    val v = x + y
    new Problem(s"SUM $x $y", s"$v")
  }

  /** mulGen generates the multiply problem for x and y.
    */
  def mulGen(x: Int, y: Int): Problem = {
    val v = x * y
    new Problem(s"MUL $x $y", s"$v")
  }

  /** revGen generates the reverse problem for x.
    */
  def revGen(x: Int): Problem = {
    val v = x.toString.toList.reverse.mkString
    new Problem(s"REV $x", s"$v")
  }

  /** incGen generates the increment problem for x.
    */
  def incGen(x: Int): Problem = {
    val v = x.toString.toList.map(_.asDigit).map(_+1).mkString
    new Problem(s"INC $x", s"$v")
  }

  /** addGen generates the digit addition problem for x.
    */
  def addGen(x: Int): Problem = {
    val v = x.toString.toList.map(_.asDigit).sum.toString
    new Problem(s"ADD $x", s"$v")
  }

  /** idxGen generates the index problem for x.
    */
  def idxGen(x: Int): Problem = {
    val xs  = x.toString.toList
    val idx = rand(0, xs.length-1, x)
    val v   = xs(idx)
    new Problem(s"IDX $x $idx", s"$v")
  }

  /** genProblem generates a random problem based on the given
    * student's 8-digit spire id.
    */
  def genProblem(id: String): Problem = {
    val r = new Random(id.toInt)
    r.nextInt(10) match {
      case 0 => fibGen(rand(10, 30, id.toInt))
      case 1 => facGen(rand(5, 10, id.toInt))
      case 2 => maxGen(rand(1, 999999, id.toInt),rand(1, 999999, 999999))
      case 3 => minGen(rand(1, 999999, id.toInt),rand(1, 999999, 999999))
      case 4 => sumGen(rand(1, 999999, id.toInt),rand(1, 999999, 999999))
      case 5 => mulGen(rand(1, 100, id.toInt),rand(1, 100, 99999))
      case 6 => revGen(rand(1, 999999, id.toInt))
      case 7 => incGen(rand(1, 999999, id.toInt))
      case 8 => addGen(rand(1, 999999, id.toInt))
      case 9 => idxGen(rand(1, 999999, id.toInt))
    }
  }

  /** writeProblem writes the problem associated with the student's
    * 8-digit ID to the assets directory. This includes both the
    * ID.send and ID.recv file.
    */
  def writeProblem(id: String, p: Problem) {
    // Create the directory:
    val assets = new File("assets")
    if (!assets.exists)
      assets.mkdir

    // Create and write the ID.send file:
    val send = new File(assets, s"$id.send")
    val sendout = new FileWriter(send)
    sendout.write(p.send)
    sendout.close

    // Create and write the ID.recv file:
    val recv = new File(assets, s"$id.recv")
    val recvout = new FileWriter(recv)
    recvout.write(p.recv)
    recvout.close
  }

  /** readStudents reads the `file` of 8-digit student IDs and returns
    * the list of student IDs as an Option.
    */
  def readStudents(file: String): Option[List[String]] = {
    try {
      val ids =
        Source.fromFile(file)
          .getLines
      Some(ids.toList)
    }
    catch {
      case _: Throwable => None
    }
  }

  /** The main function for running this program. You can easily run
    * this in sbt:
    * 
    * > run-main ProblemGenerator students.csv
    *
    * or simply `run students.csv` and choose the class to run.
    */
  def main(args: Array[String]) = {
    if (args.length != 1) {
      println("usage: scala ProblemGenerator idfile")
      sys.exit(1)
    }

    val idfile = args(0)

    readStudents(idfile) match {
      case Some(ids) => for (id <- ids) writeProblem(id, genProblem(id))
      case None => println(s"invalid file ${args(0)}")
    }
  }
}
