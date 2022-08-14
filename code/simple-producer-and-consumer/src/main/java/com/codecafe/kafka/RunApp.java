package com.codecafe.kafka;

import com.codecafe.kafka.consumer.Consumer;
import com.codecafe.kafka.producer.Producer;

import static com.codecafe.kafka.utils.Constants.BOOTSTRAP_SERVERS;

public class RunApp {

  public static void main(String[] args) {
    final String topics = "TestTopic1";
    final String consumerGroup1 = "ConsumerGroup1";
//    final String consumerGroup2 = "ConsumerGroup2";

    Producer producer = new Producer(BOOTSTRAP_SERVERS, topics);
    Consumer consumer1 = new Consumer("Consumer1", BOOTSTRAP_SERVERS, consumerGroup1, topics);
//    Consumer consumer2 = new Consumer("Consumer2", BOOTSTRAP_SERVERS, consumerGroup2, topics);

    (new Thread(producer::produceMessages)).start();

    (new Thread(consumer1::consumeMessages)).start();
//    (new Thread(consumer2::consumeMessages)).start();
  }

}