package com.codecafe.kafka.unit.producer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import com.codecafe.kafka.model.Book;
import com.codecafe.kafka.model.LibraryEvent;
import com.codecafe.kafka.producer.LibraryEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class) // mock kafkaTemplate.send() call
public class LibraryEventProducerUnitTest {

    @Mock
    private KafkaTemplate<Integer, String> kafkaTemplate;

    @Spy
    ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private LibraryEventProducer libraryEventProducer;

    @Test
    void sendLibraryEventAsyncSuccess() throws JsonProcessingException, InterruptedException, ExecutionException {

        Book book = Book.builder()
                        .bookId(101)
                        .bookAuthor("John Wick")
                        .bookName("Guns and Cars")
                        .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                                                .libraryEventId(null)
                                                .book(book)
                                                .build();

        // simulate a Success response
        String record = objectMapper.writeValueAsString(libraryEvent);

        ProducerRecord<Integer, String> producerRecord = new ProducerRecord<>("library-events", libraryEvent.getLibraryEventId(), record);

        RecordMetadata recordMetaData = new RecordMetadata(new TopicPartition("library-events", 1), 1, 1, 342, System.currentTimeMillis(), 1, 2);

        SendResult<Integer, String> sendResult = new SendResult<>(producerRecord, recordMetaData);

        SettableListenableFuture<SendResult<Integer, String>> future = new SettableListenableFuture<>();
        future.set(sendResult);

        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);

        ListenableFuture<SendResult<Integer, String>> listenableFuture = libraryEventProducer.sendLibraryEventAsync(libraryEvent);

        SendResult<Integer, String> sendResult1 = listenableFuture.get();

        assert sendResult1.getRecordMetadata().partition() == 1;

    }

    @Test
    void sendLibraryEventAsyncFailure() throws JsonProcessingException, InterruptedException, ExecutionException {

        Book book = Book.builder()
                        .bookId(101)
                        .bookAuthor("John Wick")
                        .bookName("Guns and Cars")
                        .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                                                .libraryEventId(null)
                                                .book(book)
                                                .build();

        // simulate a Runtime Exception
        SettableListenableFuture future = new SettableListenableFuture();
        future.setException(new RuntimeException("Exception while connecting to Kafka"));

        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);

        assertThrows(Exception.class, () -> libraryEventProducer.sendLibraryEventAsync(libraryEvent).get());

    }

}