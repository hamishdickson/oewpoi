package example

import oewpoi._

case class Person(id: Int, age: Int, sex: String, occupation: String)

object Example {
  import ApiIo._
  import SimpleInterpreter._
  import PoiInterpreter._
  import monix.cats._
  import monix.execution.Scheduler.Implicits.global
  import shapeless._

  def test = {
    for {
      wb <- getWorkbook("example.xlsx")
      sh <- getSheet(wb, 0)
      r <- getRows(sh)
      c <- getCells(r.tail.head)
    } yield c
  }

  val test2 = test.foldMap(run)
  val test3 = test2.runAsync

  val testId = test.foldMap(unsafePerformIO)

  val testGetAsList = get("example.xlsx").foldMap(unsafePerformIO)

  type pType = Int :: Int :: String :: String :: HNil

  val thing = 99 :: 10 :: "M" :: "Fireman" :: HNil

  val personGen = Generic[Person]
  val person2 = personGen.from(thing)

}
