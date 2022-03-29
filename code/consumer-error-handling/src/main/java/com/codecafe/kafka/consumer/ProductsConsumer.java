package com.codecafe.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
public class ProductsConsumer {

  private static final String RETRY = "-retry-0";

  private final KafkaTemplate<String, String> kafkaTemplate;

  public ProductsConsumer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaListener(topics = "products")
  public void listen(ConsumerRecord<String, String> message) {
    try {
      log.info("Received message with key : [{}], value : [{}], topic : [{}], at : [{}]",
        message.key(),
        message.value(),
        message.topic(),
        now());
      throw new RuntimeException("Failed to consume message : " + message.key() + " from topic : " + message.topic());
    } catch (Exception ex) {
      log.error("Failed to consume message with key : [{}], value : [{}], topic : [{}], at : [{}]",
        message.key(),
        message.value(),
        message.topic(),
        now());

      String productJson = "{\"code\":\"" + message.key() + "\"}";

      kafkaTemplate.send("products" + RETRY, message.key(), productJson);
    }
  }

}