# Consumer Groups

`group.id` is a mandatory attribute to start up a Consumer.

`group.id` plays a significant role when it comes to scalable message consumption.

### View the Consumer groups:

```shell
$ ./kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
console-consumer-51813
^
This is the consumer group that is automatically created when you start a Console Consumer
```

### Create two Consumers with the same group id:

```shell
$ ./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --group group-1

$ ./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --group group-1
```

So one Consumer will read from 2 partitions and another will read from other 2 partitions.
