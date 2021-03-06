Suppose these are the messages that we want to send to a topic
and we want the Consumer to receive in the same order:

Apple
Amazon
Amazing
Alpine


> Kafka Producer -> Partitioner -> topic (partition-0, partition-1, partition-2, partition-3)

If no Key is present then Partitioner will send the messages to partitions in round-robin fashion.

So,

```
Apple   -> partition-0
Amazon  -> partition-1
Amazing -> partition-2
Alpine  -> partition-3
```

There is no guarantee that the Consumer will read these messages in the same order
because Consumer polls the messages from all the partitions at the same time.


Let's now pass a Key along with every message:

```
Key:A  Value:Apple
Key:A  Value:Amazon
Key:A  Value:Amazon
Key:A  Value:Alpine
```

Now what happens is:

Kafka Producer -> Partitioner (hashing on the Key) -> topic (partition-0, partition-1, partition-2, partition-3)

Now since the Key for all the messages is same, the Partitioner will send all the messages to same partition:

```
Apple   -> partition-0
Amazon  -> partition-0
Amazing -> partition-0
Alpine  -> partition-0
```

So, same key always resolves to the same partition!


PRODUCER:

```
C:\kafka_2.13-2.8.0\bin\windows>kafka-console-producer.bat --broker-list localhost:9092 --topic test-topic --property "key.separator=-" --property "parse.key=true"
>A-Apple
>A-Amazon
>A-Amazing
>A-Alpine
```

CONSUMER:

```
C:\kafka_2.13-2.8.0\bin\windows>kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test-topic --from-beginning -property "key.separator= - " --property "print.key=true"
A - Apple
A - Amazon
A - Amazing
A - Alpine
```
