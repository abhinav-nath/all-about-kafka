package com.codecafe.kafka.unit.controller;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

        when(libraryEventProducer.sendLibraryEventAsync(isA(LibraryEvent.class))).thenReturn(null);

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

        when(libraryEventProducer.sendLibraryEventAsync(isA(LibraryEvent.class))).thenReturn(null);

        String expectedErrorMessage = "book - must not be null";

        testBadRequestError(json, expectedErrorMessage);
    }

    @Test
    @DisplayName("bookObjectCannotBeEmpty")
    void createLibraryEvent_EmptyBook() throws Exception {

        LibraryEvent libraryEvent = LibraryEvent.builder()
                                                .libraryEventId(null)
                                                .book(new Book())
                                                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);

        when(libraryEventProducer.sendLibraryEventAsync(isA(LibraryEvent.class))).thenReturn(null);

        mockMvc.perform(post("/v1/libraryevent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("bookIdIsMandatory")
    void createLibraryEvent_BookIdMandatory() throws Exception {

        Book book = Book.builder()
                        .bookId(null)
                        .bookAuthor("John Wick")
                        .bookName("Guns and Cars")
                        .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                                                .libraryEventId(null)
                                                .book(book)
                                                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);

        when(libraryEventProducer.sendLibraryEventAsync(isA(LibraryEvent.class))).thenReturn(null);

        String expectedErrorMessage = "book.bookId - must not be null";

        testBadRequestError(json, expectedErrorMessage);
    }

    @Test
    @DisplayName("bookNameIsMandatory")
    void createLibraryEvent_BookNameMandatory() throws Exception {

        Book book = Book.builder()
                        .bookId(101)
                        .bookAuthor("John Wick")
                        .bookName("")
                        .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                                                .libraryEventId(null)
                                                .book(book)
                                                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);

        when(libraryEventProducer.sendLibraryEventAsync(isA(LibraryEvent.class))).thenReturn(null);

        String expectedErrorMessage = "book.bookName - must not be blank";

        testBadRequestError(json, expectedErrorMessage);
    }

    @Test
    @DisplayName("bookIdAndNameIsMandatory")
    void createLibraryEvent_BookIdAndBookNameMandatory() throws Exception {

        Book book = Book.builder()
                        .bookId(null)
                        .bookAuthor("John Wick")
                        .bookName(null)
                        .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                                                .libraryEventId(null)
                                                .book(book)
                                                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);

        when(libraryEventProducer.sendLibraryEventAsync(isA(LibraryEvent.class))).thenReturn(null);

        String expectedErrorMessage = "book.bookId - must not be null, book.bookName - must not be blank";

        testBadRequestError(json, expectedErrorMessage);
    }

    private void testBadRequestError(String json, String expectedErrorMessage) throws Exception {
        mockMvc.perform(post("/v1/libraryevent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(expectedErrorMessage));
    }

}