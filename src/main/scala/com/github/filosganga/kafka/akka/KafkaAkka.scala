package com.github.filosganga.kafka.akka

import akka.actor._
import kafka.producer.ProducerConfig
import java.util.{UUID, Properties}
import kafka.consumer.ConsumerConfig
import com.github.filosganga.kafka.Event

/**
 *
 * @author Filippo De Luca - fdeluca@expedia.com
 */
object KafkaAkka extends App {

  val system = ActorSystem("kfka-akka")

  val producer = system.actorOf(Props(new ProducerActor(producerConfig)), "producer")
  val consumer = system.actorOf(Props(new ConsumerActor(consumerConfig)), "consumer")


  private def consumerConfig = {

    val consumerProperties = new Properties()
    consumerProperties.setProperty("group.id", "kafka-akka-1")
    consumerProperties.setProperty("zookeeper.connect", "localhost:2181")

    new ConsumerConfig(consumerProperties)
  }

  private def producerConfig = {

    val producerProperties = new Properties()
    producerProperties.setProperty("metadata.broker.list", "localhost:9092")
    producerProperties.setProperty("request.required.acks", "0")

    new ProducerConfig(producerProperties)
  }

  consumer ! ConsumerActor.Consume

  (0 until 100).foreach {
    i =>
      producer ! ProducerActor.Produce(Event(UUID.randomUUID().toString, Event.Type("TestEvent", "1.0.0"), "Hello World"))
  }

  Thread.sleep(5000)

  system.shutdown()
}
