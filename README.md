# oewpoi

Pronounced "oh-eew-pie", oewpoi is an over engineered wrapper around [Apacahe POI](https://poi.apache.org/). This project is aimed at simplifying the pretty unfriendly api in POI as well as providing a useful place for me to play with ideas around library design

If you are after a scala library for POI today, don't use this - instead I suggest you have a look at [poi.scala](https://github.com/folone/poi.scala) (or better yet, write your own and help out the next guy!)

## Example

```scala
import oewpoi._

val foo = for {
    wb <- getWorkbook("example.xlsx")
    sh <- getSheet(wb, 1)
    r <- getRows(sh)
} yield r

foo.foldMap(unsafePerofrmIO)
```


Credit: the idea for this library comes from discussions with the folks at [ASI Data Science](theasi.co)
