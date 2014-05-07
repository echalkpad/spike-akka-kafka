package com.github.filosganga.kafka

import Event._

/**
 *
 * @author Filippo De Luca - fdeluca@expedia.com
 */
case class Event(id: String, timestamp: Long, tipe: Type, payload: String) {

}

object Event {

  case class Type(name: String, version: String)

}