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
    Consumer consumerA = new Consumer("ConsumerA", BOOTSTRAP_SERVERS, consumerGroup1, topics);
//    Consumer consumerB = new Consumer("ConsumerB", BOOTSTRAP_SERVERS, consumerGroup2, topics);

    (new Thread(producer::produceMessages)).start();

    (new Thread(consumerA::consumeMessages)).start();
//    (new Thread(consumerB::consumeMessages)).start();
  }

}