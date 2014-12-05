
name := "graphique"

scalaVersion := "2.11.4"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "net.coobird" % "thumbnailator" % "0.4.7",
  "commons-codec" % "commons-codec" % "1.10",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test",
  "org.apache.commons" % "commons-lang3" % "3.3.2" % "test"
)
