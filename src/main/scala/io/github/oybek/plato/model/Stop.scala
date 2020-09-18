package io.github.oybek.plato.model

case class Stop(name: String,
                url: Option[String],
                geo: Geo)

case class Geo(latitude: Float,
               longitude: Float)
