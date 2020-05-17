package io.github.oybek.plato.model

sealed trait TransportT

object TransportT {

  case object Bus extends TransportT
  case object Tram extends TransportT
  case object Troll extends TransportT

  def emoji(t: TransportT): String =
    t match {
      case Bus   => "🚌"
      case Tram  => "🚋"
      case Troll => "🚎"
    }

  def russian(t: TransportT): String =
    t match {
      case Bus   => "автобус"
      case Tram  => "трамвай"
      case Troll => "троллейбус"
    }
}
