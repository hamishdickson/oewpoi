package oewpoi

import java.io.{File, FileInputStream}
import scala.collection.JavaConverters._

import cats._
import Oewpoi._
import org.apache.poi.xssf.usermodel._

object SimpleInterpreter {
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
          sheet.iterator.asScala.toList
        }
        case GetCells(row) => {
          row.cellIterator().asScala.toList
        }
        case Get(fileName) => {
          // todo see if we can reuse logic here
          val file = new FileInputStream(new File(fileName))
          val wb = new XSSFWorkbook(file)
          val sh = wb.getSheetAt(0)
          val rows = sh.iterator.asScala.toList
          // todo clearly wrong
          rows.head.cellIterator.asScala.toList
        }
      }
    }
}
