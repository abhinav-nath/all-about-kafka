package com.codecafe.kafka;

import com.codecafe.kafka.consumer.Consumer;
import com.codecafe.kafka.producer.Producer;

import static com.codecafe.kafka.utils.Constants.BOOTSTRAP_SERVERS;

public class RunApp {

    public static void main(String[] args) {
        String topics = "TestTopic1";
        String consumerGroup = "ConsumerGroup";

        Producer producer = new Producer(BOOTSTRAP_SERVERS, topics);
        Consumer consumer1 = new Consumer("Consumer1", BOOTSTRAP_SERVERS, consumerGroup, topics);

        (new Thread(producer::produceMessages)).start();

        (new Thread(consumer1::consumeMessages)).start();
    }

}