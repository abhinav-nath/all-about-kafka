package com.codecafe.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class KafkaNonBlockingRetriesAndDltTest extends KafkaTestBase {

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Test
  void testNonBlockingRetriesAndDlt() throws InterruptedException, ExecutionException {
    kafkaTemplate.send("products", "product1", "This is Product 1").get();
//    kafkaTemplate.send("products", "product2", "This is Product 2");
//    kafkaTemplate.send("products", "product1", "This is new Product 1");

    Thread.sleep(20000);

    try (Consumer<String, String> consumer = createConsumer()) {
      KAFKA_BROKER.consumeFromAllEmbeddedTopics(consumer);
      ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer, 10_000, 5);

      List<String> topics = getTopicsFrom(records);

      assertThat(topics)
        .containsOnly(
          "products", "products-retry", "products-dlt");
    }
  }

  private List<String> getTopicsFrom(ConsumerRecords<String, String> records) {
    List<String> topics =
      StreamSupport.stream(records.spliterator(), false)
                   .map(ConsumerRecord::topic)
                   .collect(toList());
    return topics;
  }

}