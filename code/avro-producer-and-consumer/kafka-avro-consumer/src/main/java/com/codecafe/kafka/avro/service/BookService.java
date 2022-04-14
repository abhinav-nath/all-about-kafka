package com.codecafe.kafka.avro.service;

import com.codecafe.avro.BookKey;
import com.codecafe.avro.BookValue;
import com.codecafe.kafka.avro.repository.BookRepository;
import com.codecafe.kafka.avro.entity.BookEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookService {

  private final BookRepository bookRepository;

  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  // Perform local retries before pushing the message to the retry topic
  @Retryable(maxAttempts = 5)
  public void handleEventsFromMainTopic(ConsumerRecord<BookKey, BookValue> message) {
    log.info("==> Entered inside handleEventsFromMainTopic method");
    handleEvents(message);
  }

  public void handleEventsFromRetryTopic(ConsumerRecord<BookKey, BookValue> message) {
    log.info("==> Entered inside handleEventsFromRetryTopic method");
    handleEvents(message);
  }

  private void handleEvents(ConsumerRecord<BookKey, BookValue> message) {
    log.info("handleEvents :: message with key : {} received", message.key().getIsbn());
    BookEntity bookEntity = new BookEntity();
    bookEntity.setIsbn(message.key().getIsbn());
    bookEntity.setTitle(message.value().getTitle());
    bookEntity.setAuthor(message.value().getAuthor());
    bookRepository.save(bookEntity);
  }

}