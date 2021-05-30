package com.codecafe.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codecafe.kafka.model.LibraryEvent;
import com.codecafe.kafka.model.LibraryEventType;
import com.codecafe.kafka.producer.LibraryEventProducer;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LibraryEventsController {

    @Autowired
    private LibraryEventProducer libraryEventProducer;

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> createLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws Exception {

        libraryEvent.setLibraryEventType(LibraryEventType.NEW);

        // invoke Kafka Producer
        log.info("before sendLibraryEvent");

        libraryEventProducer.sendLibraryEventAsync(libraryEvent);

        //SendResult<Integer, String> sendResult = libraryEventProducer.sendLibraryEventSync(libraryEvent);

        //SendResult<Integer, String> sendResult = libraryEventProducer.sendLibraryEventToTopic(libraryEvent);
        //log.info("sendResult is : {}", sendResult.toString());

        log.info("after sendLibraryEvent");

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

}