package com.codecafe.kafka.avro.consumer;

import com.codecafe.avro.BookKey;
import com.codecafe.avro.BookValue;
import com.codecafe.kafka.avro.service.BookService;
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
public class BookConsumer {

  private final BookService bookService;

  public BookConsumer(BookService bookService) {
    this.bookService = bookService;
  }

  @RetryableTopic(
    attempts = "4",
    backoff = @Backoff(delay = 1000),
    fixedDelayTopicStrategy = FixedDelayStrategy.SINGLE_TOPIC
  )
  @KafkaListener(topics = {"books"})
  public void listen(ConsumerRecord<BookKey, BookValue> message) {
    log.info("Received message with key : [{}], value : [{}], topic : [{}], offset : [{}], at : [{}]",
      message.key(),
      message.value(),
      message.topic(),
      message.offset(),
      now());

    if ("books".equals(message.topic()))
      bookService.handleEventsFromMainTopic(message);
    else
      bookService.handleEventsFromRetryTopic(message);
  }

  @DltHandler
  public void dlt(ConsumerRecord<BookKey, BookValue> message) {
    log.info("DLT Received message with key : [{}], value : [{}], topic : [{}], offset : [{}], at : [{}]",
      message.key(),
      message.value(),
      message.topic(),
      message.offset(),
      now());
  }

}
