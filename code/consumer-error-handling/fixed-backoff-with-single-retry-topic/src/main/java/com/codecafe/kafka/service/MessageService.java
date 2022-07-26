package com.codecafe.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService {

  // Perform local retries before pushing the message to the retry topic
  @Retryable(maxAttempts = 5)
  public void handleMessageFromMainTopic(ConsumerRecord<String, String> message) {
    log.info("==> Entered inside handleMessageFromMainTopic method");
    handleMessage(message);
  }

  public void handleMessageFromRetryTopic(ConsumerRecord<String, String> message) {
    log.info("==> Entered inside handleMessageFromRetryTopic method");
    handleMessage(message);
  }

  private void handleMessage(ConsumerRecord<String, String> message) {
    //if (!"This is Message 1".equals(message.value()))
    throw new RuntimeException("handleMessage :: Failed to process message : " + message.key() + " from topic : " + message.topic());
  }

}