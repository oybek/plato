import sbt._

object Dependencies {

  object V {
    val scalaTest = "3.2.0"
    val jsoup = "1.7.2"
  }

  val scalaTest = "org.scalatest" %% "scalatest" % V.scalaTest % Test
  val jsoup = "org.jsoup" % "jsoup" % V.jsoup

  val common = Seq(scalaTest, jsoup)
}
