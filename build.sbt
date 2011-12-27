organization := "org.daros"

name := "dispatch-achievo"

version := "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "net.databinder" %% "dispatch-core" % "0.8.6",
  "net.databinder" %% "dispatch-http" % "0.8.6",
  "net.databinder" %% "dispatch-jsoup" % "0.8.6",
  "org.jsoup" % "jsoup" % "1.6.1",
  "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2",
  "joda-time" % "joda-time" % "1.6.2",
  "org.scalatest" %% "scalatest" % "1.6.1" % "test",
  "junit" % "junit" % "4.8.2" % "test"
)