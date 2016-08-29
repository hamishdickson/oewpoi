name := "oewpoi"

version := "0.0.1"

scalaVersion := "2.11.8"

lazy val poiVersion = "3.14"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.6.0",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.apache.poi" %  "poi"      % poiVersion,
  "org.apache.poi" %  "poi-ooxml" % poiVersion
)
