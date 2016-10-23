package dragons

import java.io.{File, FileInputStream}

import scala.collection.JavaConversions._

import cats._
import cats.implicits._
import org.apache.poi.ss.usermodel.{Cell, Row}
import org.apache.poi.xssf.usermodel._

/**
  * Please don't use this - it's nasty
  * 
  * This is the kinda of horrible code you'd have to write if you use poi "out the box"
  */
object Dragons {
  sealed trait PoiCell
  case class StrCell(s: String) extends PoiCell
  case class NumCell(d: Double) extends PoiCell
  case class BoolCell(b: Boolean) extends PoiCell
  case object BlankCell extends PoiCell

  case class ErrorCell(e: Byte) extends PoiCell

  def getWorkbook(fileName: String): XSSFWorkbook = {
    val file = new FileInputStream(new File(fileName))
    new XSSFWorkbook(file)
  }

  def numSheets(wb: XSSFWorkbook): Int = wb.getNumberOfSheets()

  def getSheet(wb: XSSFWorkbook, id: Int): XSSFSheet = wb.getSheetAt(id)

  /**
   * In case you don't know, this gives you some real crap
   */
  def getRows(sheet: XSSFSheet): List[Row] = sheet.iterator.toList

  def getCells(row: Row): List[Cell] = row.cellIterator().toList

  def printCells(rows: List[Row]): List[String] =
    for {
      r <- rows
      c <- getCells(r)
    } yield c.show

  def getPCell(c: Cell): PoiCell = c.getCellType() match {
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
