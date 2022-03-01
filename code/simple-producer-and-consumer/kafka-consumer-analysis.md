# Analysis of Kafka Consumer

Create below components using the `docker-compose.yml`

1. Zookeeper
2. Kafka Server
3. Kafdrop (UI for Kafka)

```shell
docker-compose up -d
```

!["Run docker-compose.yml"](./images/run-docker-compose.png "Run docker-compose.yml")

## Case 1 : One Partition and One Consumer in One Consumer Group
Create a new topic named **TestTopic1** with **1 partition** from **Kafdrop** UI:

!["Create a topic from Kafdrop"](./images/create-topic-in-kafdrop.png "Create a topic from Kafdrop")

```java
String topics = "TestTopic1";
String consumerGroup = "ConsumerGroup";

Producer producer = new Producer(BOOTSTRAP_SERVERS, topics);
Consumer consumer1 = new Consumer("Consumer1", BOOTSTRAP_SERVERS, consumerGroup, topics);

(new Thread(producer::produceMessages)).start();
(new Thread(consumer1::consumeMessages)).start();
```

Output:

```
Consumer1 - key: [apple] value: [this is message #0] partition: [0] offset: [0]
Consumer1 - key: [apple] value: [this is message #1] partition: [0] offset: [1]
Consumer1 - key: [apple] value: [this is message #2] partition: [0] offset: [2]
Consumer1 - key: [apple] value: [this is message #3] partition: [0] offset: [3]
Consumer1 - key: [apple] value: [this is message #4] partition: [0] offset: [4]
Consumer1 - key: [apple] value: [this is message #5] partition: [0] offset: [5]
Consumer1 - key: [apple] value: [this is message #6] partition: [0] offset: [6]
Consumer1 - key: [apple] value: [this is message #7] partition: [0] offset: [7]
Consumer1 - key: [apple] value: [this is message #8] partition: [0] offset: [8]
Consumer1 - key: [apple] value: [this is message #9] partition: [0] offset: [9]
```

> **Observation**: As there is only one partition, it is assigned to the single consumer and thus Consumer1 receives all the messages.

## Case 2 : Two Partitions and One Consumer in One Consumer Group
Create a new topic - **TestTopic2** with **2 partitions**:

!["Create a topic with two partitions"](./images/create-topic-in-kafdrop_1.png "Create a topic with two partitions")

```java
String topics = "TestTopic2";
String consumerGroup = "ConsumerGroup";

Producer producer = new Producer(BOOTSTRAP_SERVERS, topics);
Consumer consumer1 = new Consumer("Consumer1", BOOTSTRAP_SERVERS, consumerGroup, topics);
Consumer consumer2 = new Consumer("Consumer2", BOOTSTRAP_SERVERS, consumerGroup, topics);

(new Thread(producer::produceMessages)).start();

(new Thread(consumer1::consumeMessages)).start();
(new Thread(consumer2::consumeMessages)).start();
```

Output:

```
Consumer1 - key: [apple] value: [this is message #0] partition: [1] offset: [0]
Consumer1 - key: [apple] value: [this is message #1] partition: [1] offset: [1]
Consumer1 - key: [apple] value: [this is message #2] partition: [1] offset: [2]
Consumer1 - key: [apple] value: [this is message #3] partition: [1] offset: [3]
Consumer1 - key: [apple] value: [this is message #4] partition: [1] offset: [4]
Consumer1 - key: [apple] value: [this is message #5] partition: [1] offset: [5]
Consumer1 - key: [apple] value: [this is message #6] partition: [1] offset: [6]
Consumer1 - key: [apple] value: [this is message #7] partition: [1] offset: [7]
Consumer1 - key: [apple] value: [this is message #8] partition: [1] offset: [8]
Consumer1 - key: [apple] value: [this is message #9] partition: [1] offset: [9]
```

> **Observation**: There are 2 partitions and one consumer.
>  All the messages have the same partitioning key as `apple` so all the messages go to only one of the two partitions.
>  And since there is only one consumer, the partition is assigned to Consumer1 and hence Consumer1 receives all the messages.
