package oewpoi

final case class Cell[A](data: A)
final case class Row[A](cells: Iterator[Cell[A]])
final case class Sheet[A](rows: Iterator[Row[A]])
final case class Workbook(fileLocation: String)


sealed trait PoiF[A]
case class GetSheet[A](id: Int) extends PoiF[Sheet[A]]
case class GetRow[A](sheet: Sheet[A], id: Int) extends PoiF[Row[A]]
case class GetCells[A](r: Row[A]) extends PoiF[List[Cell[A]]]

/*
object Oewpoi {
  import cats.free.Free
  import cats.free.Free.liftF

  type Poi[A] = Free[PoiF, A]

  def get[T](id: Int): Poi[Option[T]] = liftF[PoiF, Option[T]](GetSheet[T](id))

  import cats.{Id, ~>}

  def impureCompiler: PoiF ~> Id = new (PoiF ~> Id) {
    // impure stuff here
     def apply[A](fa: PoiF[A]): Id[A] = fa match {
       case GetSheet(id) => Some(Sheet(Iterator(Row())))
     }
  }
}
*/
