package io.github.oybek.plato.model

import scala.concurrent.duration.FiniteDuration

case class Arrival(route: String, time: FiniteDuration, transportT: TransportT)
