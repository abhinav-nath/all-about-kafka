# Kafka commands

Add the below properties in the `server.properties` for broker configuration

```
listeners=PLAINTEXT://localhost:9092
auto.create.topics.enable=false
```

### Start up the Zookeeper

from the bin directory

```shell
zookeeper-server-start ../config/zookeeper.properties
```

### Start up the Kafka Broker

```shell
kafka-server-start ../config/server.properties
```

### Create a topic

```shell
kafka-topics --create --topic test-topic -zookeeper localhost:2181 --replication-factor 1 --partitions 4
```

### Create a Console Producer

**Without Key**

```shell
kafka-console-producer --broker-list localhost:9092 --topic test-topic
```

**With Key**

```shell
kafka-console-producer --broker-list localhost:9092 --topic test-topic --property "key.separator=-" --property "parse.key=true"
```

### Create a Console Consumer

**Without Key**

```shell
kafka-console-consumer --bootstrap-server localhost:9092 --topic test-topic --from-beginning
```

**With Key**

```shell
kafka-console-consumer --bootstrap-server localhost:9092 --topic test-topic --from-beginning -property "key.separator= - " --property "print.key=true"
```

**With Consumer Group**

```shell
kafka-console-consumer --bootstrap-server localhost:9092 --topic test-topic --group <group-name>
```

### List the topics in a cluster

```shell
kafka-topics --zookeeper localhost:2181 --list
```

### Describe topic

**Describe all the topics:**

```shell
kafka-topics --zookeeper localhost:2181 --describe
```

**Describe a specific topic:**

```shell
kafka-topics --zookeeper localhost:2181 --describe --topic <topic-name>
```

**Alter the min insync replica:**

```shell
kafka-topics --alter --zookeeper localhost:2181 --topic library-events --config min.insync.replicas=2
```

### Delete a topic

```shell
kafka-topics --zookeeper localhost:2181 --delete --topic <topic-name>
```

### View consumer groups

```shell
kafka-consumer-groups --bootstrap-server localhost:9092 --list
```

### Consumer Groups and their Offsets

```shell
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group console-consumer-27773
```

### Delete Consumer Group

```shell
kafka-consumer-groups --bootstrap-server localhost:9092 --delete --group my-group --group my-other-group
```

### View the Commit Log

```shell
kafka-run-class kafka.tools.DumpLogSegments --deep-iteration --files /tmp/kafka-logs/test-topic-0/00000000000000000000.log
```

### Setting the Minimum Insync Replica

```shell
kafka-configs --alter --zookeeper localhost:2181 --entity-type topics --entity-name test-topic --add-config min.insync.replicas=2
```
