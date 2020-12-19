package io.github.oybek.plato.dumper

import java.io.PrintWriter

import io.github.oybek.plato.model.{Geo, Stop}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BustimeDumperSpec extends AnyFlatSpec with Matchers {

  "City" must "be parsed well" in {
    val stops = BustimeDumper.dumpCity("helsinki")
    new PrintWriter("dump.sql") {
      write("insert into stop (name, latitude, longitude, url, city_id) values\n")
      stops.getOrElse(List.empty[Stop]).foreach {
        case Stop(name, Some(url), Geo(latitude, longitude)) =>
          write(s"('$name', $latitude, $longitude, '$url', 4),\n")
        case _ => ()
      }
      close()
    }
  }
}
