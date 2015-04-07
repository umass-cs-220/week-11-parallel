import java.io._
import scala.io._
import scala.util._

object ProblemGenerator {

  case class Problem(send: String, recv: String)

  def rand(from: Int, to: Int, seed: Int): Int = {
    val r = new Random(seed)
    from + r.nextInt((to - from) + 1)
  }

  def fib(n: Int, nxt: Int, res: Int): Int = {
    n match {
      case 0 => res
      case _ => fib(n-1, nxt+res, nxt)
    }
  }

  def fac(n: Int): Int = {
    def fac2(acc: Int, n: Int): Int =
      n match {
        case 0 => acc
        case _ => fac2(n*acc, n-1)
      }
    fac2(1, n)
  }

  def max(x: Int, y: Int): Int = 
    if (x > y) x else y

  def min(x: Int, y: Int): Int = 
    if (x < y) x else y

  def fibGen(n: Int): Problem = {
    val v = fib(n, 1, 0)
    new Problem(s"FIB $n", s"$v")
  }

  def facGen(n: Int): Problem = {
    val v = fac(n)
    new Problem(s"FAC $n", s"$v")
  }

  def maxGen(x: Int, y: Int): Problem = {
    val v = max(x, y)
    new Problem(s"MAX $x $y", s"$v")
  }

  def minGen(x: Int, y: Int): Problem = {
    val v = min(x, y)
    new Problem(s"MIN $x $y", s"$v")
  }

  def sumGen(x: Int, y: Int): Problem = {
    val v = x + y
    new Problem(s"SUM $x $y", s"$v")
  }

  def mulGen(x: Int, y: Int): Problem = {
    val v = x * y
    new Problem(s"MUL $x $y", s"$v")
  }

  def revGen(x: Int): Problem = {
    val v = x.toString.toList.reverse.mkString
    new Problem(s"REV $x", s"$v")
  }

  def incGen(x: Int): Problem = {
    val v = x.toString.toList.map(_.asDigit).map(_+1).mkString
    new Problem(s"INC $x", s"$v")
  }

  def addGen(x: Int): Problem = {
    val v = x.toString.toList.map(_.asDigit).sum.toString
    new Problem(s"ADD $x", s"$v")
  }

  def idxGen(x: Int): Problem = {
    val xs  = x.toString.toList
    val idx = rand(0, xs.length-1, x)
    val v   = xs(idx)
    new Problem(s"IDX $x $idx", s"$v")
  }

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

  def writeProblem(id: String, p: Problem) {
    val assets = new File("assets")
    if (!assets.exists)
      assets.mkdir
    
    val send = new File(assets, s"$id.send")
    val sendout = new FileWriter(send)
    sendout.write(p.send)
    sendout.close

    val recv = new File(assets, s"$id.recv")
    val recvout = new FileWriter(recv)
    recvout.write(p.recv)
    recvout.close
  }

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

  def main(args: Array[String]) = {
    readStudents(args(0)) match {
      case Some(ids) => for (id <- ids) writeProblem(id, genProblem(id))
      case None => println(s"invalid file ${args(0)}")
    }
  }
}
