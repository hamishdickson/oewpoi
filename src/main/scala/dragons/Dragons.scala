package dragons

import java.io.{FileInputStream, File}

import org.apache.poi.xssf.usermodel._
import org.apache.poi.ss.usermodel.{Row, Cell}

import scala.collection.JavaConversions._

import cats._
import cats.data._
import cats.implicits._

/**
 * This is the kind of horrible code you'd have to write if you use poi "out the box"
 * It's horrible and involves mutable state everywhere
 */
object Dragons {
  sealed trait PCell
  case class StrCell(s: String) extends PCell
  case class NumCell(d: Double) extends PCell
  case class BoolCell(b: Boolean) extends PCell
  case object BlackCell extends PCell
  case class ErrorCell(e: Byte) extends PCell

  lazy val getWorkbook: String => XSSFWorkbook = fileName => {
    val file = new FileInputStream(new File(fileName))
    new XSSFWorkbook(file)
  }

  def numSheets(wb: XSSFWorkbook): Int = wb.getNumberOfSheets()
  def getSheet(wb: XSSFWorkbook, id: Int): XSSFSheet = wb.getSheetAt(id)

  /**
   * In case you don't know, this gives you real crap
   */
  def getRows(sheet: XSSFSheet): List[Row] = sheet.iterator.toList

  def getCells(row: Row): List[Cell] = row.cellIterator().toList

  def printCells(rows: List[Row]): List[String] =
    for {
      r <- rows
      c <- getCells(r)
    } yield c.show

  def getPCell(c: Cell): PCell = c.getCellType() match {
    case Cell.CELL_TYPE_STRING => StrCell(c.getStringCellValue())
    case Cell.CELL_TYPE_NUMERIC => NumCell(c.getNumericCellValue())
    case Cell.CELL_TYPE_ERROR => ErrorCell(c.getErrorCellValue())
    case Cell.CELL_TYPE_BLANK => BlackCell
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
