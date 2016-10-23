package oewpoi

import java.io.{File, FileInputStream}

import scala.collection.JavaConversions._

import cats.free.Free
import cats.free.Free._
import cats.{Id, ~>}
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

  // first (kinda dumb interpreter)
  def unsafePerformIO: Poi ~> Id =
    new (Poi ~> Id) {
      def apply[A](fa: Poi[A]): Id[A] = fa match {
        case GetWorkbook(fileName) => {
          println(s"Getting file $fileName")
          val file = new FileInputStream(new File(fileName))
          new XSSFWorkbook(file)
        }
        case GetSheet(wb, id) => {
          println(s"Getting sheet $wb $id")
          wb.getSheetAt(id)
        }
      }
    }
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
