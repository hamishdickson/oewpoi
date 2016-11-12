name := "oewpoi"

version := "0.0.1"

scalaVersion := "2.12.0"

//scalaOrganization in ThisBuild := "org.typelevel"
//scalacOptions += "-Ypartial-unification" // enable fix for SI-2712
//scalacOptions += "-Yliteral-types"       // enable SIP-23 implementation

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

lazy val catsVersion = "0.8.1"
lazy val poiVersion = "3.14"
lazy val monixVersion = "2.1.0"
lazy val shapelessVersion = "2.3.2"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % catsVersion,
  "org.apache.poi" %  "poi"      % poiVersion,
  "org.apache.poi" %  "poi-ooxml" % poiVersion,
  "io.monix" %% "monix" % monixVersion,
  "io.monix" %% "monix-cats" % monixVersion,
  "com.chuusai" %% "shapeless" % shapelessVersion,
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)
