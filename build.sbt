name := """play-scala-seed"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

// Cambiar la versión de Scala a 3.3.1 para coincidir con el JAR
scalaVersion := "3.3.1"

// Añadir la opción del compilador para TASTy
scalacOptions ++= Seq("-Ytasty-reader")
javacOptions ++= Seq("-source", "19", "-target", "19")

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
