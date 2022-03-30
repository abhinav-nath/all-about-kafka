package com.codecafe.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.header.Header;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class KafkaNonBlockingRetriesAndDltTest extends KafkaTestBase {

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Test
  void testNonBlockingRetriesAndDltWithFixedBackOff() {
    kafkaTemplate.send("products", "product1", "This is Product1");
    kafkaTemplate.send("products", "product2", "This is Product2");
    kafkaTemplate.send("products", "product1", "This is new Product1");

    try (Consumer<String, String> consumer = createConsumer()) {
      KAFKA_BROKER.consumeFromAllEmbeddedTopics(consumer);
      ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer, 10_000, 5);

      List<String> topics = getTopicsFrom(records);

      assertThat(topics)
        .containsOnly(
          "products", "products-retry", "products-retry.DLT");
    }
  }

  private List<String> getTopicsFrom(ConsumerRecords<String, String> records) {
    List<String> topics =
      StreamSupport.stream(records.spliterator(), false)
                   .peek(
                     record -> {
                       log.info("Topic: {}", record.topic());
                       log.info("Key: {}", record.key());
                       log.info("Value: {}", record.value());
                       log.info(
                         "Header: {}={}",
                         KafkaHeaders.ORIGINAL_OFFSET,
                         headerValue(record, KafkaHeaders.ORIGINAL_OFFSET).map(this::bytesToLong));
                       log.info(
                         "Header: {}={}",
                         KafkaHeaders.ORIGINAL_TIMESTAMP,
                         headerValue(record, KafkaHeaders.ORIGINAL_TIMESTAMP)
                           .map(this::bytesToLong)
                           .map(Instant::ofEpochMilli));
                       log.info(
                         "Header: {}={}",
                         KafkaHeaders.ORIGINAL_TIMESTAMP_TYPE,
                         headerValue(record, KafkaHeaders.ORIGINAL_TIMESTAMP_TYPE)
                           .map(this::bytesToString));
                       log.info(
                         "Header: {}={}",
                         KafkaHeaders.ORIGINAL_TOPIC,
                         headerValue(record, KafkaHeaders.ORIGINAL_TOPIC).map(this::bytesToString));
                       log.info(
                         "Header: {}={}",
                         KafkaHeaders.ORIGINAL_PARTITION,
                         headerValue(record, KafkaHeaders.ORIGINAL_PARTITION).map(this::bytesToInt));
                       log.info(
                         "Header: {}={}",
                         KafkaHeaders.EXCEPTION_FQCN,
                         headerValue(record, KafkaHeaders.EXCEPTION_FQCN).map(this::bytesToString));
                       log.info(
                         "Header: {}={}",
                         KafkaHeaders.EXCEPTION_MESSAGE,
                         headerValue(record, KafkaHeaders.EXCEPTION_MESSAGE)
                           .map(this::bytesToString));
                     })
                   .map(ConsumerRecord::topic)
                   .collect(toList());
    return topics;
  }

  private Optional<byte[]> headerValue(ConsumerRecord<String, String> record, String headerKey) {
    return Optional.ofNullable(record.headers().lastHeader(headerKey)).map(Header::value);
  }

  private Integer bytesToInt(byte[] bytes) {
    return ByteBuffer.wrap(bytes).getInt();
  }

  private Long bytesToLong(byte[] bytes) {
    return ByteBuffer.wrap(bytes).getLong();
  }

  private String bytesToString(byte[] bytes) {
    return new String(bytes);
  }

}