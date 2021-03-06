package com.codecafe.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.FixedDelayStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.codecafe.kafka.service.MessageService;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
public class MessageConsumer {

  private final MessageService messageService;

  public MessageConsumer(MessageService messageService) {
    this.messageService = messageService;
  }

  @RetryableTopic(
    attempts = "4",
    backoff = @Backoff(delay = 1000),
    fixedDelayTopicStrategy = FixedDelayStrategy.SINGLE_TOPIC,
    autoCreateTopics = "false"
  )
  @KafkaListener(topics = {"test-topic"})
  public void listen(ConsumerRecord<String, String> message) {
    log.info("Received message with key : [{}], value : [{}], topic : [{}], offset : [{}], at : [{}]",
      message.key(),
      message.value(),
      message.topic(),
      message.offset(),
      now());

    /* The method marked as @Retryable cannot be called within the same class.
       It will not work!
       Method marked as @Retryable must be present in a Spring bean,
       and it must be invoked from another Spring bean.
       Then only it will work.
       https://stackoverflow.com/a/38755319/10371864
     */
    if ("test-topic".equals(message.topic()))
      messageService.handleMessageFromMainTopic(message);
    else
      messageService.handleMessageFromRetryTopic(message);
  }

  @DltHandler
  public void dlt(ConsumerRecord<String, String> message) {
    log.info("DLT Received message with key : [{}], value : [{}], topic : [{}], offset : [{}], at : [{}]",
      message.key(),
      message.value(),
      message.topic(),
      message.offset(),
      now());
  }

}