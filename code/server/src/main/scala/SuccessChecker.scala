import java.io._
import scala.io._
import scala.util._

/** The SuccessChecker checks the success of students solving their
  * generated problems. Running this application will produce output
  * that looks like this:
  *  {{{
  *  X  12345678 MIN 443868 238719 238719 0 0
  *  X  87654321 FAC 8 40320 0 0
  *  X  19283746 IDX 891006 5 6 0 0
  *  G  11111111 MAX 187398 238719 238719 0 238719
  *  X  22222222 REV 788759 957887 0 0
  *  X  33333333 MAX 560634 238719 560634 0 0
  *  X  44444444 SUM 489994 238719 728713 0 0
  *  X  55555555 REV 820812 218028 0 0
  *  X  66666666 MUL 86 42 3612 0 0
  *  X  77777777 MAX 58607 238719 238719 0 0
  *  X  88888888 IDX 247635 1 4 0 0
  *  X  99999999 MUL 76 42 3192 0 0
  *  }}}
  *
  *  The format is:
  *
  *  STATUS SPIREID PROBLEM... FAILURES SOLUTION
  *
  *  STATUS    : Will be one of X (not successful) or G (success)
  *  SPIREID   : Student's 8-digit spire ID
  *  PROBLEM...: The original problem to solve
  *  FAILURES  : The number of failed attempts
  *  SOLUTION  : The submitted solution from the student
  */
object SuccessChecker {

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

  /** Read a single line from the file given the filename.
    */
  def getLine(file: String): Option[String] = {
    val f = new File(file)
    if (f.exists)
      Some(Source.fromFile(file).getLines().next())
    else
      Some("0")
  }
 
  /** Main function for running the checker. Easy enough to do from sbt:
    *
    * > run-main SuccessChecker students.csv
    * 
    */
  def main(args: Array[String]) = {
    if (args.length != 1) {
      println("usage: scala SuccessChecker file")
      sys.exit(1)
    }

    // Read the file containing the student IDs:
    readStudents(args(0)) match {
      case Some(ids) =>
        // Iterate over the IDs reading the first line in each of the
        // files associated with that student ID:
        for (id <- ids) {
          val recv = getLine(s"assets/$id.recv")
          val send = getLine(s"assets/$id.send")
          val fail = getLine(s"assets/$id.fail")
          val succ = getLine(s"assets/$id.succ")
          (send, recv, fail, succ) match {
            case (Some(a), Some(b), Some(c), Some(d)) =>
              // d will be 0 if the file did not exist (problem not solved)
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
