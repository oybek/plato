package io.github.oybek.plato.parser

import java.time.LocalTime

import io.github.oybek.plato.model.Arrival
import io.github.oybek.plato.model.TransportT.{Bus, Tram, Troll}
import org.jsoup.Jsoup
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source
import scala.concurrent.duration._

class Parser001Spec extends FlatSpec with Matchers {
  val blockHtml = Source.fromResource("htmlBlock1.html").mkString
  val blockHtml2 = Source.fromResource("htmlBlock2.html").mkString

  "Bustime block parse" must "work ok" in {
    Parser001.parseBlock(LocalTime.of(20, 35), Jsoup.parse(blockHtml)) shouldBe Some(
      "Мира" -> List(
        Arrival("4", 2 minutes, Tram),
        Arrival("20", 4 minutes, Tram),
        Arrival("18", 5 minutes, Tram),
      )
    )
    Parser001.parseBlock(LocalTime.of(23, 30), Jsoup.parse(blockHtml2)) shouldBe Some(
      "Первомайская" -> List(Arrival("28", 3 minutes, Bus))
    )
  }

  "Bustime parse page" must "work ok" in {
    val page1 = Source.fromResource("htmlPage1.html").mkString
    val page2 = Source.fromResource("htmlPage2.html").mkString

    Parser001.parse(Jsoup.parse(page1)) shouldBe List(
      ("Троицкий", List(
        Arrival("43", 0 minutes, Bus),
        Arrival("3", 32 minutes, Troll))
      ),
      ("Лермонтовский д.57", List(
          Arrival("100", 0 minutes, Bus),
          Arrival("10", 4 minutes, Bus),
          Arrival("8" , 8 minutes, Troll),
          Arrival("3", 23 minutes, Troll))
      )
    )
    Parser001.parse(Jsoup.parse(page2)) shouldBe List()
  }
}
