Kafka Akka Example
==================

Create Topic
------------

    bin/kafka-topics.sh --create --zookeeper 192.168.4.11:2181 --replication-factor 2 --partitions 10 --topic event