package oewpoi

import java.io.{File, FileInputStream}

import scala.collection.JavaConversions._

import cats.free.Free
import cats.free.Free._
import org.apache.poi.ss.usermodel.{Cell, Row}
import org.apache.poi.xssf.usermodel._


object Oewpoi {
  type SheetId = Int

  sealed trait Poi[A]
  case class GetWorkbook(fileName: String) extends Poi[XSSFWorkbook]
  case class GetSheet(wb: XSSFWorkbook, id: SheetId) extends Poi[XSSFSheet]

  type PoiF[A] = Free[Poi, A]

  def getWorkbook(fileName: String): PoiF[XSSFWorkbook] =
    liftF[Poi, XSSFWorkbook](GetWorkbook(fileName))

  def getSheet(wb: XSSFWorkbook, id: SheetId): PoiF[XSSFSheet] =
    liftF[Poi, XSSFSheet](GetSheet(wb, id))

}

object Example {
  import Oewpoi._

  def test = {
    for {
      wb <- getWorkbook("example.xlsx")
      sh <- getSheet(wb, 1)
    } yield sh
  }
}
