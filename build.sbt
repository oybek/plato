ThisBuild / version := "0.1"
ThisBuild / organization := "io.github.oybek"

val settings = Compiler.settings ++ Seq()

lazy val plato = (project in file("."))
  .settings(name := "plato")
  .settings(libraryDependencies ++= Dependencies.common)
