package oewpoi

import cats._

import monix.eval._

import java.io.{File, FileInputStream}
import Oewpoi._
import org.apache.poi.xssf.usermodel._
import scala.collection.JavaConverters._


object PoiInterpreter {
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
          Task { sheet.iterator.asScala.toList }
        case GetCells(row) =>
          Task { row.cellIterator().asScala.toList }
        case Get(fileName) => Task {
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
