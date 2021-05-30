package com.codecafe.kafka.integration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import com.codecafe.kafka.model.Book;
import com.codecafe.kafka.model.LibraryEvent;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"library-events"}, partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
"spring.kafka.admin.properties.bootstrap-servers=${spring.embedded.kafka.brokers}"})
public class LibraryEventsControllerEventsTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void createLibraryEventTest() {

        Book book = Book.builder()
                        .bookId(101)
                        .bookAuthor("John Wick")
                        .bookName("Guns and Cars")
                        .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                                                .libraryEventId(null)
                                                .book(book)
                                                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent, headers);

        ResponseEntity<LibraryEvent> responseEntity = restTemplate.exchange("/v1/libraryevent", HttpMethod.POST, request, LibraryEvent.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

}