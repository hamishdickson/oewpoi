# oewpoi

Pronounced "oh-eew-pie", oewpoi is an over engineered wrapper for [Apacahe POI](https://poi.apache.org/) in scala. This project is aimed at simplifying the pretty unfriendly api in POI as well as providing a useful place for me to play with ideas around library design

If you are after a scala library for POI today, don't use this - instead I suggest you have a look at [poi.scala](https://github.com/folone/poi.scala) (or better yet, write your own and help out the next guy!)

## Example

```scala
import oewpoi._
import monix.cats._

val foo = for {
    wb <- getWorkbook("example.xlsx")
    sh <- getSheet(wb, 1)
    r <- getRows(sh)
    c <- getCells(r.head)
} yield c

foo.foldMap(run).runAsync
```

This is a work in progress and the aim is to be able to say something like the following

```scala
case class Bar(id: Int, age: Int, sex: String, occupation: String)

val file = new FileInputStream(new File("myfile.xlsx"))

val rows = 
  for {
    wb <- getWorkbook(file)
    sh <- getSheet(wb, 1)
    r <- getRows(sh)
  } yield r

implicit val listBarCells = new Cells[List, Bar] = ??? 

val bars: List[Bar] = get[List, Bar](rows)

implicit val rddBarCells = new Cells[RDD, Bar] = ???

val rddBars: RDD[Bar] = get[RDD, Bar](rows)
```

or something... I don't know... I'm still working on it


Credit: the idea for this library comes from discussions with the folks at [ASI Data Science](theasi.co)
