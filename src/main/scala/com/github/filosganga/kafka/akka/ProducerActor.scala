package com.github.filosganga.kafka.akka

import akka.actor.{ActorLogging, Actor}
import kafka.producer._
import com.github.filosganga.kafka.{KeyCodec, EventCodec, Protocol, Event}

class ProducerActor(pc: ProducerConfig) extends Actor with ActorLogging{

  import ProducerActor._

  private val producer = createProducer(pc)

  override def receive = {
    case Produce(event) =>

      log.info(s"Producing event: $event")

      producer.send(createMessage(event))
  }

  private def createMessage(event: Event): KeyedMessage[Protocol.Event.Key, Protocol.Event] = {

    val key = Protocol.Event.Key.newBuilder()
      .setId(event.id)
      .setTimestamp(event.timestamp)
      .setType(event.tipe.name)
      .setVersion(event.tipe.version)
      .build()

    val message = Protocol.Event.newBuilder()
      .setPayload(event.payload)
      .build()

    new KeyedMessage("event", key, message)
  }

  override def postStop() {
    producer.close()
  }
}

object ProducerActor {

  case class Produce(event: Event)

  private def createProducer(pc: ProducerConfig) = {

    val pps = pc.props.props
    pps.setProperty("serializer.class", classOf[EventCodec].getName)
    pps.setProperty("key.serializer.class", classOf[KeyCodec].getName)

    val producerConfig = new ProducerConfig(pps)

    new Producer[Protocol.Event.Key, Protocol.Event](producerConfig)
  }

}
