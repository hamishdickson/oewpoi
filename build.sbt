name := "oewpoi"

version := "0.0.1"

scalaVersion := "2.11.8"

lazy val catsVersion = "0.7.0"
lazy val poiVersion = "3.14"
lazy val monixVersion = "2.0.5"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % catsVersion,
  "org.apache.poi" %  "poi"      % poiVersion,
  "org.apache.poi" %  "poi-ooxml" % poiVersion,
  "io.monix" %% "monix" % monixVersion,
  "io.monix" %% "monix-cats" % monixVersion,
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)
