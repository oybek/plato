package io.github.oybek.plato.parser

import io.github.oybek.plato.model.Arrival
import org.jsoup.nodes.Document

trait Parser {
  def parse(html: Document): List[(String, List[Arrival])]
}
