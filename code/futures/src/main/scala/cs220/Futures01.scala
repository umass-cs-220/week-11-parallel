package cs220

import scala.util.Try

object Coffee01 {

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
  def grind(beans: CoffeeBeans): GroundCoffee =
    s"ground coffee of $beans"
  def heatWater(water: Water) = water.copy(temp = 85)
  def frothMilk(milk: Milk): FrothedMilk = s"frothed $milk"
  def brew(coffee: GroundCoffee, heatWater: Water): Espresso = "espresso"
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
  def prepareCappuccino: Try[Cappuccino] =
    for {
      ground   <- Try(grind("arabica beans"))
      water    <- Try(heatWater(Water(25)))
      espresso <- Try(brew(ground, water))
      foam     <- Try(frothMilk("milk"))
    } yield combine(espresso, foam)
}
