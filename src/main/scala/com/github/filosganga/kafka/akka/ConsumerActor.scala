package com.github.filosganga.kafka.akka

import akka.actor.{ActorLogging, Actor}
import kafka.consumer._
import kafka.message.MessageAndMetadata

import com.github.filosganga.kafka.{Protocol, Event, EventCodec, KeyCodec}
import scala.util.{Failure, Try}
import kafka.utils.VerifiableProperties

/**
 *
 * @author Filippo De Luca - fdeluca@expedia.com
 */
class ConsumerActor(consumerConfig: ConsumerConfig) extends Actor with ActorLogging {

  import ConsumerActor._

  private val consumer = createConsumer(consumerConfig)
  private val stream = createStream("event")

  private def createStream(topic: String) = {
    val streams = consumer.createMessageStreams(
      Map(topic->1),
      new KeyCodec(new VerifiableProperties()),
      new EventCodec(new VerifiableProperties())
    )

    streams(topic)(0)
  }

  override def receive = {
    case Consume =>

      val iterator = stream.iterator()

      Try(iterator.next()).recoverWith {
        case cte: ConsumerTimeoutException =>
          Failure(cte)
        case t: Throwable =>
          log.error("Error consuming", t)
          Failure(t)
      }.foreach {message =>
        process(parseEventType(message), parseEvent(message))
      }

      self ! Consume

    case Commit =>
      consumer.commitOffsets
  }

  private def parseEventType(mm: MessageAndMetadata[Protocol.Event.Key, Protocol.Event]): Event.Type = {

    val key = mm.key()

    Event.Type(key.getType, key.getVersion)
  }

  private def parseEvent(mm: MessageAndMetadata[Protocol.Event.Key, Protocol.Event]): Event = {

    val message = mm.message()

    Event("", parseEventType(mm), message.getPayload)
  }

  private def process(eventType: Event.Type, event: => Event) {
    event match {
      case Event(_, _, payload) =>
        log.info(s"Received Event: $payload")
    }
  }

  override def postStop() {
    consumer.shutdown()
  }

}

object ConsumerActor {

  case object Consume

  case object Commit

  def createConsumer(cc: ConsumerConfig) = {

    val ps = cc.props.props
    ps.setProperty("consumer.timeout.ms", "1000")
    ps.setProperty("auto.commit.enable", "true")

    Consumer.create(new ConsumerConfig(ps))
  }

}
