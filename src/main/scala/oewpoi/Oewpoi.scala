package oewpoi

import java.io.{File, FileInputStream}

import scala.collection.JavaConversions._

import cats._
import cats.implicits._

import cats.free.Free
import cats.free.Free._
import cats.{Id, ~>}
import org.apache.poi.ss.usermodel.{Cell, Row}
import org.apache.poi.xssf.usermodel._

import monix.eval._

object Oewpoi {
  type SheetId = Int
  type Rows = List[Row]
  type Cells = List[Cell]

  sealed trait Poi[A]
  case class GetWorkbook(fileName: String) extends Poi[XSSFWorkbook]
  case class GetSheet(wb: XSSFWorkbook, id: SheetId) extends Poi[XSSFSheet]
  case class GetRows(sheet: XSSFSheet) extends Poi[Rows]
  case class GetCells(row: Row) extends Poi[Cells]
  case class Get(fileName: String) extends Poi[Cells]

  type PoiF[A] = Free[Poi, A]

  def getWorkbook(fileName: String): PoiF[XSSFWorkbook] =
    liftF[Poi, XSSFWorkbook](GetWorkbook(fileName))

  def getSheet(wb: XSSFWorkbook, id: SheetId): PoiF[XSSFSheet] =
    liftF[Poi, XSSFSheet](GetSheet(wb, id))

  def getRows(sheet: XSSFSheet): PoiF[Rows] =
    liftF[Poi, Rows](GetRows(sheet))

  def getCells(row: Row): PoiF[Cells] =
    liftF[Poi, Cells](GetCells(row))

  def get(fileName: String): PoiF[Cells] =
    liftF[Poi, Cells](Get(fileName))

  // first (kinda dumb interpreter)
  def unsafePerformIO: Poi ~> Id =
    new (Poi ~> Id) {
      def apply[A](fa: Poi[A]): Id[A] = fa match {
        case GetWorkbook(fileName) => {
          val file = new FileInputStream(new File(fileName))
          new XSSFWorkbook(file)
        }
        case GetSheet(wb, id) => {
          wb.getSheetAt(id)
        }
        case GetRows(sheet) => {
          sheet.iterator.toList
        }
        case GetCells(row) => {
          row.cellIterator().toList
        }
        case Get(fileName) => {
          // todo see if we can reuse logic here
          val file = new FileInputStream(new File(fileName))
          val wb = new XSSFWorkbook(file)
          val sh = wb.getSheetAt(0)
          val rows = sh.iterator.toList
          // todo clearly wrong
          rows.head.cellIterator.toList
        }
      }
    }

  def run: Poi ~> Task =
    new (Poi ~> Task) {
      def apply[A](fa: Poi[A]): Task[A] = fa match {
        case GetWorkbook(fileName) =>
          Task {
            val file = new FileInputStream(new File(fileName))
            new XSSFWorkbook(file)
          }
        case GetSheet(wb, id) =>
          Task { wb.getSheetAt(id) }
        case GetRows(sheet) =>
          Task { sheet.iterator.toList }
        case GetCells(row) =>
          Task { row.cellIterator().toList }
        case Get(fileName) => Task {
          // todo see if we can reuse logic here
          val file = new FileInputStream(new File(fileName))
          val wb = new XSSFWorkbook(file)
          val sh = wb.getSheetAt(0)
          val rows = sh.iterator.toList
          // todo clearly wrong
          rows.head.cellIterator.toList
        }
      }
    }
}

object TypelevelPoi {
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
