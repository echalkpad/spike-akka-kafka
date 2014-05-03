package com.github.filosganga.kafka

import kafka.serializer._
import kafka.utils.VerifiableProperties

trait Codec[T] extends Decoder[T] with Encoder[T]

class KeyCodec(props: VerifiableProperties) extends Codec[Protocol.Event.Key] {

  override def fromBytes(bytes: Array[Byte]) =
    Protocol.Event.Key.parseFrom(bytes)

  override def toBytes(key: Protocol.Event.Key) =
    key.toByteArray
}

class EventCodec(props: VerifiableProperties) extends Codec[Protocol.Event] {

  override def toBytes(event: Protocol.Event) =
    event.toByteArray

  override def fromBytes(bytes: Array[Byte]) =
    Protocol.Event.parseFrom(bytes)
}
