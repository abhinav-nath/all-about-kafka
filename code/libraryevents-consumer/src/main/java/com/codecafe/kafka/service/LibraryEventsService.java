package com.codecafe.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codecafe.kafka.persistence.dao.LibraryEventsRepository;
import com.codecafe.kafka.persistence.entity.LibraryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LibraryEventsService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LibraryEventsRepository libraryEventsRepository;

    public void processLibraryEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonMappingException, JsonProcessingException {

        LibraryEvent libraryEvent = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);
        log.info("libraryEvent : {}", libraryEvent);

        switch(libraryEvent.getLibraryEventType()) {

        case NEW:
            save(libraryEvent);
            break;

        case UPDATE:

            break;

        default:
            log.info("Invalid libraryEventType");
        }
    }

    private void save(LibraryEvent libraryEvent) {
        // build the mapping between book and libraryEvent
        libraryEvent.getBook().setLibraryEvent(libraryEvent);
        libraryEventsRepository.save(libraryEvent);
        log.info("Successfully persisted the libraryEvent : {}", libraryEvent);        
    }

}