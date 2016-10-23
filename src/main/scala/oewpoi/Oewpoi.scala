package oewpoi

import java.io.{File, FileInputStream}

import scala.collection.JavaConversions._

import cats._
import cats.data._
import cats.implicits._

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
  case class GetRows(sheet: XSSFSheet) extends Poi[List[Row]]
  case class GetCells(row: Row) extends Poi[List[Cell]]

  type PoiF[A] = Free[Poi, A]

  def getWorkbook(fileName: String): PoiF[XSSFWorkbook] =
    liftF[Poi, XSSFWorkbook](GetWorkbook(fileName))

  def getSheet(wb: XSSFWorkbook, id: SheetId): PoiF[XSSFSheet] =
    liftF[Poi, XSSFSheet](GetSheet(wb, id))

  def getRows(sheet: XSSFSheet): PoiF[List[Row]] =
    liftF[Poi, List[Row]](GetRows(sheet))

  def getCells(row: Row): PoiF[List[Cell]] =
    liftF[Poi, List[Cell]](GetCells(row))
}

object CellStuff {
  // ADT describing the cell types available
  sealed trait PoiCell
  case class StrCell(s: String) extends PoiCell
  case class NumCell(d: Double) extends PoiCell
  case class BoolCell(b: Boolean) extends PoiCell
  case class ErrorCell(e: Byte) extends PoiCell
  case object BlankCell extends PoiCell

  def getPoiCell(c: Cell): PoiCell = c.getCellType() match {
    case Cell.CELL_TYPE_STRING => StrCell(c.getStringCellValue())
    case Cell.CELL_TYPE_NUMERIC => NumCell(c.getNumericCellValue())
    case Cell.CELL_TYPE_ERROR => ErrorCell(c.getErrorCellValue())
    case Cell.CELL_TYPE_BLANK => BlankCell
    case Cell.CELL_TYPE_BOOLEAN => BoolCell(c.getBooleanCellValue())
  }

  implicit val showCell = new Show[Cell] {
    def show(c: Cell): String = c.getCellType() match {
      case Cell.CELL_TYPE_STRING => c.getStringCellValue()
      case Cell.CELL_TYPE_NUMERIC => c.getNumericCellValue().show
      case Cell.CELL_TYPE_ERROR => c.getErrorCellValue().show
      case Cell.CELL_TYPE_BLANK => "".show
      case Cell.CELL_TYPE_BOOLEAN => c.getBooleanCellValue().show
    }
  }
}

object Example {
  import Oewpoi._

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
        case GetRows(sheet) => {
          println(s"Getting rows from $sheet")
          sheet.iterator.toList
        }
        case GetCells(row) => {
          println(s"Getting cells from $row")
          row.cellIterator().toList
        }
      }
    }

  def test = {
    for {
      wb <- getWorkbook("example.xlsx")
      sh <- getSheet(wb, 1)
      r <- getRows(sh)
      _ = println(r)
    } yield r
  }
}
