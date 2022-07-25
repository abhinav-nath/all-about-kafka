package com.codecafe.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
public class MessageConsumer {

  @KafkaListener(topics = {"${app.kafka.topic.name}"})
  public void listen(ConsumerRecord<String, String> message) {
    log.info("Received message with key : [{}], value : [{}], topic : [{}], offset : [{}], at : [{}]",
      message.key(),
      message.value(),
      message.topic(),
      message.offset(),
      now());
  }

}