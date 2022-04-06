package com.codecafe.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductService {

  // Perform local retries before pushing the message to the retry topic
  @Retryable(maxAttempts = 5)
  public void handleProductsFromMainTopic(ConsumerRecord<String, String> message) {
    log.info("==> Entered inside handleProductsFromMainTopic method");
    handleProducts(message);
  }

  public void handleProductsFromRetryTopic(ConsumerRecord<String, String> message) {
    log.info("==> Entered inside handleProductsFromRetryTopic method");
    handleProducts(message);
  }

  private void handleProducts(ConsumerRecord<String, String> message) {
    //if (!"This is Product 1".equals(message.value()))
    throw new RuntimeException("handleProducts :: Failed to process message : " + message.key() + " from topic : " + message.topic());
  }

}