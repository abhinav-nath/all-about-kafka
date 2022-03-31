package com.codecafe.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.FixedDelayStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
public class ProductsConsumer {

  @RetryableTopic(
    attempts = "4",
    backoff = @Backoff(delay = 1000),
    fixedDelayTopicStrategy = FixedDelayStrategy.SINGLE_TOPIC)
  @KafkaListener(topics = "products", containerFactory = "localRetryContainerFactory")
  public void listen(ConsumerRecord<String, String> message) {
    try {
      log.info("Received message with key : [{}], value : [{}], topic : [{}], offset : [{}], at : [{}]",
        message.key(),
        message.value(),
        message.topic(),
        message.offset(),
        now());

      doSomething(message);

    } catch (Exception ex) {
      throw new RuntimeException("Failed to consume message : " + message.key() + " from topic : " + message.topic());
    }
  }

  private void doSomething(ConsumerRecord<String, String> message) {
    if (!"This is new Product 1".equals(message.value()))
      throw new RuntimeException("Failed to consume message : " + message.key() + " from topic : " + message.topic());
  }

  @DltHandler
  public void dlt(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    log.info("Received message : [{}] from topic : [{}] at : [{}]", message, topic, now());
  }

}