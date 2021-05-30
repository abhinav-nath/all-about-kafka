package com.codecafe.kafka.unit.controller;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.codecafe.kafka.controller.LibraryEventsController;
import com.codecafe.kafka.model.Book;
import com.codecafe.kafka.model.LibraryEvent;
import com.codecafe.kafka.producer.LibraryEventProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(LibraryEventsController.class)
@AutoConfigureMockMvc
public class LibraryEventsControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LibraryEventProducer libraryEventProducer;

    @Test
    void createLibraryEvent() throws Exception {

        Book book = Book.builder()
                        .bookId(101)
                        .bookAuthor("John Wick")
                        .bookName("Guns and Cars")
                        .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                                                .libraryEventId(null)
                                                .book(book)
                                                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);

        doNothing().when(libraryEventProducer).sendLibraryEventAsync(isA(LibraryEvent.class));

        mockMvc.perform(post("/v1/libraryevent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    }
    
    @Test
    @DisplayName("bookObjectCannotBeNull")
    void createLibraryEvent_NullBook() throws Exception {

        LibraryEvent libraryEvent = LibraryEvent.builder()
                                                .libraryEventId(null)
                                                .book(null)
                                                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);

        doNothing().when(libraryEventProducer).sendLibraryEventAsync(isA(LibraryEvent.class));

        mockMvc.perform(post("/v1/libraryevent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("bookObjectCannotBeEmpty")
    void createLibraryEvent_EmptyBook() throws Exception {

        LibraryEvent libraryEvent = LibraryEvent.builder()
                                                .libraryEventId(null)
                                                .book(new Book())
                                                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);

        doNothing().when(libraryEventProducer).sendLibraryEventAsync(isA(LibraryEvent.class));

        mockMvc.perform(post("/v1/libraryevent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }

}