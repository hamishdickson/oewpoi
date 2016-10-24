name := "oewpoi"

version := "0.0.1"

scalaVersion := "2.11.8"

lazy val catsVersion = "0.7.0"
lazy val poiVersion = "3.14"
lazy val fs2Version = "0.9.1"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % catsVersion,
  "org.apache.poi" %  "poi"      % poiVersion,
  "org.apache.poi" %  "poi-ooxml" % poiVersion,
  "co.fs2" %% "fs2-core" % fs2Version,
  "co.fs2" %% "fs2-io" % fs2Version,
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)
