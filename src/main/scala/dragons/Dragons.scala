package dragons

sealed trait Cell
case class NumCell(v: Double) extends Cell

case class Sheet(rows: List[Row])
case class Row(cells: List[Cell])
case class Workbook(sheets: List[Sheet])

object Dragons {
  def get[A](id: Int): A = ???

  def getSheet(wb: Workbook, id: Int): Sheet = ???
  def getRows(sheet: Sheet): List[Row] = ???
  def getRow(sheet: Sheet, id: Int): Row = ???
  def getCells(row: Row): List[Cell] = ???
  def getCell(row: Row, id: Int): Cell = ???
}

object Example {
  import Dragons._

  println(get[Sheet](1))
}
