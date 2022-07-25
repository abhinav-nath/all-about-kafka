package com.codecafe.kafka.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codecafe.kafka.model.Message;
import com.codecafe.kafka.producer.MessageProducer;

@RestController
@RequestMapping("/message")
public class MessageController {

  private final MessageProducer messageProducer;

  public MessageController(MessageProducer messageProducer) {
    this.messageProducer = messageProducer;
  }

  @PostMapping
  public void addBook(@RequestBody Message message) {
    messageProducer.send(message);
  }

}