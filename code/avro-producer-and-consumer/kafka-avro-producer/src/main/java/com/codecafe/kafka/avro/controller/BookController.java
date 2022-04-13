package com.codecafe.kafka.avro.controller;

import com.codecafe.avro.BookKey;
import com.codecafe.avro.BookValue;
import com.codecafe.kafka.avro.model.Book;
import com.codecafe.kafka.avro.producer.BookProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

  private final BookProducer bookProducer;

  public BookController(BookProducer bookProducer) {
    this.bookProducer = bookProducer;
  }

  @PostMapping
  public void addBook(@RequestBody Book book) {
    BookValue value = BookValue.newBuilder().setIsbn(book.getIsbn()).setTitle(book.getTitle()).setAuthor(book.getAuthor()).build();
    BookKey key = BookKey.newBuilder().setIsbn(book.getIsbn()).build();
    bookProducer.send(key, value);
  }

}