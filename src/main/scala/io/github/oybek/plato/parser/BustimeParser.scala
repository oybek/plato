package io.github.oybek.plato.parser

import java.time.LocalTime
import java.time.temporal.ChronoUnit.MINUTES

import io.github.oybek.plato.model.TransportT.{Bus, Tram, Troll}
import io.github.oybek.plato.model.{Arrival, TransportT}
import org.jsoup.nodes.{Document, Element}

import scala.jdk.CollectionConverters._
import scala.concurrent.duration._

object BustimeParser extends Parser {
  def parse(doc: Document): List[(String, List[Arrival])] = {
    val time = doc.select("div.header").asScala.toString()
    val hhmm = "[0-9]?[0-9]:[0-9][0-9]".r
      .findAllIn(time)
      .toList
      .headOption
      .map(_.split(":"))
    hhmm match {
      case Some(arr) if (arr.length == 2) =>
        val btime = LocalTime.of(arr(0).toInt, arr(1).toInt)
        doc
          .select("div.eight.wide.column")
          .iterator()
          .asScala
          .toList
          .filter(
            _.select("a.ui.labeled.small.icon.button")
              .iterator()
              .asScala
              .toList
              .nonEmpty
          )
          .flatMap(parseBlock(btime, _))
      case _ => List()
    }
  }

  def parseBlock(curTime: LocalTime,
                 element: Element): Option[(String, List[Arrival])] =
    for {
      dir <- Option(element.select("h3").first()).map(_.ownText())
      arrivals <- Option(element.select("tbody").first())
        .map { tbody =>
          tbody
            .select("tr")
            .iterator()
            .asScala
            .flatMap(parseRow(curTime, _))
            .toList
        }
    } yield dir -> arrivals

  private def parseRow(curTime: LocalTime, row: Element): Iterator[Arrival] = {
    val reachTimeOpt = extractReachTime(row).map {
      case (h, m) =>
        val reachTime = LocalTime.of(h, m)
        curTime.until(reachTime, MINUTES).max(0).minutes
    }
    val routes = extractRoutes(row)
    reachTimeOpt
      .map { reachTime =>
        routes.map {
          case (route, tt) =>
            Arrival(route, reachTime, tt)
        }
      }
      .getOrElse(Iterator.empty)
  }

  private def extractRoutes(row: Element): Iterator[(String, TransportT)] = {
    Option(row.select("td").last())
      .map(
        _.select("a")
          .iterator()
          .asScala
          .map(_.ownText().replace("+", "").filter(_.isLetterOrDigit))
          .map {
            case x if x.contains("ТВ") => x.replace("ТВ", "") -> Tram
            case x if x.contains("Т")  => x.replace("Т", "") -> Troll
            case x                     => x -> Bus
          }
      )
      .getOrElse(Iterator.empty)
  }

  private def extractReachTime(row: Element): Option[(Int, Int)] =
    for {
      td <- Option {
        row.select("td").first()
      }
      b <- Option {
        td.select("b").first()
      }
      hhmm = b.ownText().split(':')
      res <- if (hhmm.length == 2) {
        Some(hhmm(0).toInt, hhmm(1).toInt)
      } else {
        None
      }
    } yield res
}
