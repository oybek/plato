import sbt._

object Dependencies {

  object V {
    val scalaTest = "3.2.0"
    val jsoup = "1.13.1"
    val circe = "0.13.0"
  }

  val scalaTest = "org.scalatest" %% "scalatest" % V.scalaTest % Test
  val jsoup = "org.jsoup" % "jsoup" % V.jsoup
  val circe = Seq(
    "io.circe" %% "circe-core" % V.circe,
    "io.circe" %% "circe-parser" % V.circe,
    "io.circe" %% "circe-generic" % V.circe,
    "io.circe" %% "circe-generic-extras" % V.circe
  )

  val common = Seq(scalaTest, jsoup) ++ circe
}
