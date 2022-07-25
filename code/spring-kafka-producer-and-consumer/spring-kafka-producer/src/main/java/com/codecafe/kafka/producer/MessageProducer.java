package com.codecafe.kafka.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.extern.slf4j.Slf4j;

import com.codecafe.kafka.model.Message;

@Slf4j
@Component
public class MessageProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Value("${app.kafka.topic.name}")
  private String topicName;

  public MessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void send(Message message) {
    ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message.getId(), message.getText());

    future.addCallback(new ListenableFutureCallback<>() {
      @Override
      public void onFailure(Throwable ex) {
        log.error("Failed to send message", ex);
      }

      @Override
      public void onSuccess(SendResult<String, String> result) {
        log.info("Kafka message sent successfully");
      }
    });
  }

}