ThisBuild / version := "0.1.0-SNAPSHOT"

//ThisBuild / scalaVersion := "3.1.3"
//ThisBuild / scalaVersion := "3.4.1"
ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "untitled",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.18",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % "test",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0",
    libraryDependencies += "com.google.inject" % "guice" % "7.0.0",
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.2.0",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.3"
  )
