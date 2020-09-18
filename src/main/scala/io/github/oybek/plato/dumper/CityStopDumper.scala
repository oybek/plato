package io.github.oybek.plato.dumper

import io.github.oybek.plato.model.Stop

trait CityStopDumper {
  def dumpCity(cityName: String): Either[Throwable, List[Stop]]
}
