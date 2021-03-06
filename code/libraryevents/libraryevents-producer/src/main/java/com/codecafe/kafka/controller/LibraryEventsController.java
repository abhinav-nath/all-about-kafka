package com.codecafe.kafka.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codecafe.kafka.model.LibraryEvent;
import com.codecafe.kafka.model.LibraryEventType;
import com.codecafe.kafka.producer.LibraryEventProducer;

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
        //log.info("sendResult is : {}", sendResult.toString());

        log.info("after sendLibraryEvent");

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PutMapping("/v1/libraryevent")
    public ResponseEntity<?> updateLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws Exception {

        if(libraryEvent.getLibraryEventId() == null || libraryEvent.getLibraryEventId() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("libraryEventId must be not be null or 0");

        libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);

        // invoke Kafka Producer
        libraryEventProducer.sendLibraryEventAsync(libraryEvent);

        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }

}