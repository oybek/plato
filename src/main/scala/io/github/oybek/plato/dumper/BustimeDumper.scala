package io.github.oybek.plato.dumper

import cats.implicits._
import io.circe.parser._
import io.circe.generic.auto._
import io.github.oybek.plato.model.Stop
import org.jsoup.Jsoup

import scala.jdk.CollectionConverters._

object BustimeDumper extends CityStopDumper {

  override def dumpCity(cityName: String): Either[Throwable, List[Stop]] =
    for {
      url <- Either.right(formUrl(cityName))
      doc <- attempt(Jsoup.connect(url).get())
      rawLinks = doc
        .select("a.item")
        .iterator().asScala.toList
      stopLinks = rawLinks
        .filter(
          _.attributes()
            .get("href")
            .startsWith(url))
      stops <- stopLinks.map { stopLinks =>
        val url = stopLinks.attr("href")
        val stop = dumpStop(url).map(_.copy(url = Some(url)))
        println(stop)
        stop
      }.sequence
    } yield stops

  private def dumpStop(stopUrl: String): Either[Throwable, Stop] =
    for {
      doc <- attempt(Jsoup.connect(stopUrl).get())
      json = doc
        .select("script[type$=application/ld+json]")
        .html()
      res <- decode[Stop](json)
    } yield res

  private def formUrl(cityName: String): String =
    s"https://busti.me/$cityName/stop/"

  private def attempt[T](block: => T): Either[java.lang.Throwable, T] =
    try { Right(block) }
    catch { case ex: Throwable => Left(ex) }
}
