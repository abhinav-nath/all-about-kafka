package com.codecafe.kafka.producer;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.codecafe.kafka.model.LibraryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LibraryEventProducer {

    private static final String TOPIC_NAME = "library-events";

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // method 1 - async mechanism using callback
    public void sendLibraryEventAsync(LibraryEvent libraryEvent) throws JsonProcessingException {

        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key, value);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleResult(key, value, result);
            }

            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

        });
    }

    private void handleResult(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Message sent successfully for the key : {} and value : {}, partition is : {}", key, value, result.getRecordMetadata().partition());        
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error sending the message. Exception is : {}", ex.getMessage());

        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in method onFailure : {}", throwable.getMessage());
        }
    }

    // method 2 - sync mechanism
    public SendResult<Integer, String> sendLibraryEventSync(LibraryEvent libraryEvent) throws Exception {

        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        SendResult<Integer, String> sendResult = null;

        try {

            // sendDefault will use the default configured topic
            // spring.kafka.template.default-topic=library-events

            sendResult = kafkaTemplate.sendDefault(key, value).get(1, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException e) {
            log.error("InterruptedException/ExecutionException while sending the message. Exception is : {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception while sending the message. Exception is : {}", e.getMessage());
            throw e;
        }

        return sendResult;
    }

    // method 3 - using ProducerRecord
    public SendResult<Integer, String> sendLibraryEventToTopic(LibraryEvent libraryEvent) throws Exception {

        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key, value);

        SendResult<Integer, String> sendResult = null;

        try {
            // send() has the capability to send messages to a specific topic
            // good practice is to create a ProducerRecord and pass it to send()

            sendResult = kafkaTemplate.send(producerRecord).get(1, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException e) {
            log.error("InterruptedException/ExecutionException while sending the message. Exception is : {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception while sending the message. Exception is : {}", e.getMessage());
            throw e;
        }

        return sendResult;
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value) {

        // add headers to the message
        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()),
                new RecordHeader("event-timestamp", LocalDate.now().toString().getBytes()));

        return new ProducerRecord<>(TOPIC_NAME, null, key, value, recordHeaders);
    }

}