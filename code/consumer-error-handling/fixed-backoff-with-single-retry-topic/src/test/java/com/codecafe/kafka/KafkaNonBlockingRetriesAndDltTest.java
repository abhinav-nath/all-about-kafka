package com.codecafe.kafka;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class KafkaNonBlockingRetriesAndDltTest extends KafkaTestBase {

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Test
  void testNonBlockingRetriesAndDlt() throws InterruptedException, ExecutionException {
    kafkaTemplate.send("test-topic", "1", "This is Message 1").get();
//    kafkaTemplate.send("test-topic", "2", "This is Message 2");
//    kafkaTemplate.send("test-topic", "3", "This is new Message 1");

    Thread.sleep(20000);

    try (Consumer<String, String> consumer = createConsumer()) {
      KAFKA_BROKER.consumeFromAllEmbeddedTopics(consumer);
      ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer, 10_000, 5);

      List<String> topics = getTopicsFrom(records);

      assertThat(topics)
        .containsOnly(
          "test-topic", "my-prefix-test-topic-retry", "my-prefix-test-topic-dlt");
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