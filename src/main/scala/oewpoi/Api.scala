package oewpoi

import Oewpoi._

import cats.free.Free._

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel._

object ApiIo {
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
}
