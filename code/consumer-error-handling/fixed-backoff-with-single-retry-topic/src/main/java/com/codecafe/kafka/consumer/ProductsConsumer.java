package com.codecafe.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.FixedDelayStrategy;
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
  @KafkaListener(topics = "products")
  public void listen(ConsumerRecord<String, String> message) {
    log.info("Received message with key : [{}], value : [{}], topic : [{}], offset : [{}], at : [{}]",
      message.key(),
      message.value(),
      message.topic(),
      message.offset(),
      now());

    if ("products".equals(message.topic())) {
      try {
        doSomething(message);
      } catch (Exception ex) {
        retry(message);
      }
    } else {
      doSomething(message);
    }
  }

  private void retry(ConsumerRecord<String, String> message) {
    int retryCounter = 1;

    while (retryCounter <= 3) {
      try {
        doSomething(message);
        return;
      } catch (Exception ex) {
        retryCounter++;
        if (retryCounter >= 3)
          throw new RuntimeException("retry :: Failed to process message : " + message.key() + " from topic : " + message.topic());
      }
    }
  }

  private void doSomething(ConsumerRecord<String, String> message) {
    log.info("==> Entered inside doSomething method");
    //if (!"This is Product 1".equals(message.value()))
    throw new RuntimeException("doSomething :: Failed to process message : " + message.key() + " from topic : " + message.topic());
  }

  @DltHandler
  public void dlt(ConsumerRecord<String, String> message) {
    log.info("Received message with key : [{}], value : [{}], topic : [{}], offset : [{}], at : [{}]",
      message.key(),
      message.value(),
      message.topic(),
      message.offset(),
      now());
  }

}