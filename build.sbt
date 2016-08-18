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

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",       // yes, this is 2 args
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",        // N.B. doesn't work well with the ??? hole
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import"     // 2.11 only
)
