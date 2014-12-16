
name := "graphique"

version := "1-SNAPSHOT"

organization := "graphique"

scalaVersion := "2.11.4"

val akkaVersion = "2.3.7"
val sprayVersion = "1.3.2"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.2.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "org.slf4j" % "slf4j-simple" % "1.7.7",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.7",
  "net.coobird" % "thumbnailator" % "0.4.7",
  "commons-codec" % "commons-codec" % "1.10",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "io.spray" %% "spray-can" % s"$sprayVersion",
  "io.spray" %% "spray-routing" % s"$sprayVersion",
  "io.spray" %% "spray-json" % "1.3.1",
  "com.typesafe.akka" %% "akka-actor" % s"$akkaVersion",
  "com.typesafe.akka" %% "akka-testkit" % s"$akkaVersion",
  "jmimemagic" % "jmimemagic" % "0.1.2",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.9.10",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test",
  "org.apache.commons" % "commons-lang3" % "3.3.2" % "test",
  "com.mashape.unirest" % "unirest-java" % "1.3.27" % "test"
)

assemblyMergeStrategy in assembly := {
  // A conflict between Xerces jars *sigh*
  case "org/w3c/dom/html/HTMLDOMImplementation.class" =>
    MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
