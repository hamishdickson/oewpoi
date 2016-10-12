package oewpoi

//final case class Cell[A](data: A)
final case class Row[A](c: Int)
final case class Sheet[A](id: Int)
//final case class Workbook(fileLocation: String)

sealed trait PoiF[A]
case class GetSheet[A](id: Int) extends PoiF[Sheet[A]]
case class GetRow[A](sheet: Sheet[A]) extends PoiF[Row[A]]
//case class GetCells[A](r: Row[A]) extends PoiF[List[Cell[A]]]

object Oewpoi {
  import cats.free.Free
  import cats.free.Free.liftF

  type Poi[A] = Free[PoiF, A]

  def getSheet[T](id: Int): Poi[Sheet[T]] = liftF[PoiF, Sheet[T]](GetSheet[T](id))
  def getRow[T](s: Sheet[T]): Poi[Row[T]] = liftF[PoiF, Row[T]](GetRow[T](s))

  import cats.{Id, ~>}

  def impureCompiler: PoiF ~> Id = new (PoiF ~> Id) {
    // impure stuff here
     def apply[A](fa: PoiF[A]): Id[A] = fa match {
       case GetSheet(id) =>
          Sheet(id)
       case GetRow(s) =>
          Row(s.id)
    }
  }

  def program: Poi[Row[Int]] = {
    for {
      s <- getSheet[Int](1)
      r <- getRow[Int](s)
    } yield r
  }
}
