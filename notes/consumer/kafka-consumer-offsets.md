Suppose Producer sent 4 messages to the test-topic

And our Consumer (with group.id = group1) read all the messages

```
-----------------------------------------------
 test-topic
-----------------------------------------------
 partition-0   ABC DEF GHI JKL
 offset         0   1   2   3 
                            ^
-----------------------------------------------
```

Now suppose if the Consumer went down for some reason

By that time Producer sent 4 more messages:

```
-----------------------------------------------
 test-topic
-----------------------------------------------
 partition-0   ABC DEF GHI JKL MNO PQR STU VWX
 offset         0   1   2   3   4   5   6   7
                            ^
-----------------------------------------------
```

And now Consumer comes back up again.

So, HOW DOES THE CONSUMER KNOW THAT IT HAS TO START READING MESSAGES FROM OFFSET 4 ?


Once the Consumer reads all the polled messages, it commits the offset to a special topic called -->  __consumer_offsets along with the group.id


Now when the Consumer comes back again, it reads the messages from offset 4 by looking up the last committed offset in the __consumer_offsets topic.


Consumer offsets behave like a bookmark for the consumer to start reading the messages the the point it left off.

```
 test-topic
-----------------------------------------------
 partition-0   ABC DEF GHI JKL MNO PQR STU VWX
 offset         0   1   2   3   4   5   6   7
                                ^
-----------------------------------------------
```

`__consumer_offsets` topic is auto-created by the broker.

```
C:\kafka_2.13-2.8.0\bin\windows>kafka-topics.bat --zookeeper localhost:2181 --list
__consumer_offsets
test-topic
```
