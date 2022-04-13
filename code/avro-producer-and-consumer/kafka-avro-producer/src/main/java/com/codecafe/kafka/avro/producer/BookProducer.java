package com.codecafe.kafka.avro.producer;

import com.codecafe.avro.BookKey;
import com.codecafe.avro.BookValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class BookProducer {

  @Value("${app.kafka.topic.name}")
  private String topicName;

  private final KafkaTemplate<BookKey, BookValue> kafkaTemplate;

  public BookProducer(KafkaTemplate<BookKey, BookValue> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void send(BookKey key, BookValue value) {
    ListenableFuture<SendResult<BookKey, BookValue>> future = kafkaTemplate.send(topicName, key, value);

    future.addCallback(new ListenableFutureCallback<>() {
      @Override
      public void onFailure(Throwable ex) {
        log.error("Failed to send message", ex);
      }

      @Override
      public void onSuccess(SendResult<BookKey, BookValue> result) {
        log.info("Kafka message sent successfully");
      }
    });
  }

}