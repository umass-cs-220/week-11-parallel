import java.io._
import scala.io._
import scala.util._

object SuccessChecker {

  def readStudents(file: String): Option[List[String]] = {
    try {
      // Get a list of Spire IDs from the given file:
      val ids = Source.fromFile(file)
        .getLines()           // First, get the lines in the file
        .drop(1)              // Drop the first header line
        .map(_.split(",")(2)) // Split each line and grab the 2nd entry
      // return the ids in an Option:
      Some(ids.toList)
    }
    catch {
      case _: Throwable => None
    }
  }

  def getLine(file: String): Option[String] = {
    val f = new File(file)
    if (f.exists)
      Some(Source.fromFile(file).getLines().next())
    else
      Some("0")
  }

  def main(args: Array[String]) = {
    if (args.length != 1) {
      println("usage: scala SuccessChecker file")
      sys.exit(1)
    }

    readStudents(args(0)) match {
      case Some(ids) =>
        for (id <- ids) {
          val recv = getLine(s"assets/$id.recv")
          val send = getLine(s"assets/$id.send")
          val fail = getLine(s"assets/$id.fail")
          val succ = getLine(s"assets/$id.succ")
          (send, recv, fail, succ) match {
            case (Some(a), Some(b), Some(c), Some(d)) =>
              if (d == "0")
                print("X  ")
              else
                print("G  ")
              println(s"$id $a $b $c $d")
            case _ =>
              println(s"Problem for $id.")
          }


        }
      case None => println(s"could not read ids from ${args(0)}")
    }
  }
}
