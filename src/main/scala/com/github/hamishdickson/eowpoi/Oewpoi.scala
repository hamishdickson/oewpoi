package oewpoi

sealed trait PoiA[A]
case class Get[T](id: Int) extends PoiA[Option[T]]

object Oewpoi {
  import cats.free.Free
  import cats.free.Free.liftF

  type Poi[A] = Free[PoiA, A]

  def get[T](id: Int): Poi[Option[T]] = liftF[PoiA, Option[T]](Get[T](id))
}
