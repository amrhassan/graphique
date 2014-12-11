
name := "graphique"

scalaVersion := "2.11.4"

val akkaVersion = "2.3.7"
val sprayVersion = "1.3.2"

libraryDependencies ++= Seq(
  "net.coobird" % "thumbnailator" % "0.4.7",
  "commons-codec" % "commons-codec" % "1.10",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "io.spray" %% "spray-can" % s"$sprayVersion",
  "io.spray" %% "spray-routing" % s"$sprayVersion",
  "io.spray" %% "spray-json" % "1.3.1",
  "com.typesafe.akka" %% "akka-actor" % s"$akkaVersion",
  "com.typesafe.akka" %% "akka-testkit" % s"$akkaVersion",
  "jmimemagic" % "jmimemagic" % "0.1.2",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test",
  "org.apache.commons" % "commons-lang3" % "3.3.2" % "test"
)
