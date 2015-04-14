package cs220

import scala.util.Try

import scala.concurrent.future
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Random

object Coffee02 {

  type CoffeeBeans = String
  type GroundCoffee = String
  type Milk = String
  type FrothedMilk = String
  type Espresso = String
  type Cappuccino = String

  case class Water(temp: Int)

  //
  // Steps for making cappuccino:
  //
  def grind(beans: CoffeeBeans): Future[GroundCoffee] =
    Future {
      println("starting grinding...")
      Thread.sleep(Random.nextInt(2000))
      if (beans == "baked beans")
        throw GrindingException("are you joking?")
      println("finished grinding...")
      s"ground coffee of $beans"
    }

  /*
   What is going on here? This is how Future is defined:

   object Future {
     def apply[T](body: => T)(implicit ectx: ExecutionContext): Future[T]
   }

   This allows us to create a Future like this:

   Future(() => println("hello"))

   Or, more simply as:

   Future {
     println("hello")
   }

   */

  def heatWater(water: Water): Future[Water] =
    Future {
      println("heating the water now")
      Thread.sleep(Random.nextInt(2000))
      println("hot, it's hot!")
      water.copy(temp = 85)
    }

  def frothMilk(milk: Milk): Future[FrothedMilk] =
    Future {
      println("milk frothing system engaged")
      Thread.sleep(Random.nextInt(2000))
      println("shutting down milk frothing system")
      s"frothed $milk"
    }

  def brew(coffee: GroundCoffee, heatWater: Water): Future[Espresso] =
    Future {
      println("happy brewing ☺")
      Thread.sleep(Random.nextInt(2000))
      println("it's brewed! ☕ ")
      "espresso"
    }

  def combine(espress: Espresso, frothedMilk: FrothedMilk): Cappuccino = "cappuccino"

  //
  // Some exceptions for things that might go wrong in the individual steps
  //
  case class GrindingException(msg: String) extends Exception(msg)
  case class FrothingException(msg: String) extends Exception(msg)
  case class WaterBoilingException(msg: String) extends Exception(msg)
  case class BrewingException(msg: String) extends Exception(msg)

  //
  // Going through the steps sequentially...
  //
  // def prepareCappuccinoSequentially: Try[Cappuccino] =
  //   for {
  //     ground   <- Try(grind("arabica beans"))
  //     water    <- Try(heatWater(Water(25)))
  //     espresso <- Try(brew(ground, water))
  //     foam     <- Try(frothMilk("milk"))
  //   } yield combine(espresso, foam)

  def prepareCappuccino: Future[Cappuccino] = {
    val groundCoffee = grind("arabica beans")
    val heatedWater  = heatWater(Water(20))
    val frothedMilk  = frothMilk("milk")
    for {
      ground   <- groundCoffee
      water    <- heatedWater
      foam     <- frothedMilk
      espresso <- brew(ground, water)
    } yield combine(espresso, foam)
  }

  //
  // Some more explanation...
  //

  //
  // CALLBACKS
  //

  // How do you work with Futures directly:
  def doGrind = {
    /*
     Future.onSuccess takes a "callback" function and executes it
     after the future has successfully completed.
     */
    grind("arabica beans").onSuccess {
      case ground => println(s"okay, got my ground coffee $ground")
    }
  }

  // Here is an example that handles failures:
  import scala.util.{Success, Failure}
  def bakedGrind = {
    grind("baked beans").onComplete {
      case Success(ground) => println(s"got my $ground")
      case Failure(exc)    => println(s"we got a problem: ${exc.getMessage}")
    }
    println("When did we get here?")
  }

  // You can also compose futures in a functional way. In this example
  // we create a Future for heating the water and map its result to an
  // anonymous function that automatically creates a new Future[Boolean].
  def tempOKMap =
    heatWater(Water(25)).map {
      water => println("we're in the future!")
               (80 to 85).contains(water.temp)
    }

  // This creates a Future[Boolean]
  def tempOK(water: Water): Future[Boolean] =
    Future {
      (80 to 85).contains(water.temp)
    }

  // This creates a Future[Future[Boolean]]
  def nestedFuture: Future[Future[Boolean]] =
    heatWater(Water(25)).map {
      water => tempOK(water)
    }

  // This is what we use if there are dependencies are results. It is
  // essentially the same as the for comprehension above. The for
  // comprehension is much easier to, um, comprehend :-)
  def flatFuture: Future[Boolean] =
    heatWater(Water(25)).flatMap {
      water => tempOK(water)
    }

}
