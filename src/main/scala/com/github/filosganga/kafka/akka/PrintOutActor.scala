package com.github.filosganga.kafka.akka

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.github.filosganga.kafka.Event

/**
 *
 * @author Filippo De Luca - fdeluca@expedia.com
 */
class PrintOutActor extends Actor {

  override def receive = {
    case Event(_, _, _, payload) =>
      println(s"Message: $payload")
  }
}
