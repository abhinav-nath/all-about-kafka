## Kafka Cluster

At the heart of Kafka, there is a Kafka cluster.

Kafka cluster generally consists of multiple Brokers.


## Zookeeper

In order to manage multiple Brokers, we need a Zookeeper.

Zookeeper keeps track of the health of the brokers and manage the cluster for you.


## Broker

Broker is what all the Kafka clients will interact with.


## Client APIs

1. Kafka Producer

   Producer writes data into Kafka topic.

2. Kafka Consumer

   Consumer consumes the messages from Kafka topic.


## Topics

Topics live inside the Kafka Brokers.

Producers send messages to Kafka Topics.

Consumers read messages from Kafka Topics.


## Partitions

Partition is where the message lives inside the topic.

Each topic in general can have one or more partitions.

Each partition is an ordered, immutable sequence of records,
that means once a record is produced, it cannot be changed.

Each record is assigned a sequential number called 'offset'.

Each partition is independent of each other.

" Ordering is only guaranteed only at the partition level. "

Partitions continue to grow as new records are produced.

All the records are persisted in a physical log file (commit log)
in the file system where Kafka is installed.


## Kafka Message

Kafka message sent from producer has two properties:

1. Key (optional)
2. Value

If we send messages to a topic without the key then ordering is not guaranteed as 
the messages may go to different partitions.

If we want our messages to be ordered then we need to send all the messages with the same key.

This way all the messages will go to the same partition and hence ordering will be maintained.

Remember - "Ordering is only guaranteed only at the partition level."
