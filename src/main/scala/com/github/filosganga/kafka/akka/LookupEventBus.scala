package com.github.filosganga.kafka.akka

import com.github.filosganga.kafka

import akka.event.{LookupClassification, EventBus}
import akka.actor.ActorRef

class LookupEventBus extends EventBus with LookupClassification {

  override type Event = kafka.Event
  override type Classifier = kafka.Event.Type
  override type Subscriber = ActorRef

  override protected def classify(event: Event) =
    event.tipe

  // will be invoked for each event for all subscribers which registered themselves
  // for the event’s classifier
  override protected def publish(event: Event, subscriber: Subscriber) {
    subscriber ! event
  }

  // must define a full order over the subscribers, expressed as expected from
  // `java.lang.Comparable.compare`
  override protected def compareSubscribers(a: Subscriber, b: Subscriber) =
    a.compareTo(b)

  // determines the initial size of the index data structure
  // used internally (i.e. the expected number of different classifiers)
  override protected def mapSize() = 128

}
