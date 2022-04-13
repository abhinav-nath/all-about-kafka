package com.codecafe.kafka.avro.error;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.lang.Nullable;

@Slf4j
public class ConsumerErrorHandler extends LoggingErrorHandler {

  @Override
  public void handle(Exception exception, @Nullable ConsumerRecord<?, ?> consumerRecord) {
    if (consumerRecord != null) {
      final String message = String.format("Error while processing consumerRecord with: Key [%s], Topic [%s], Partition [%s], Offset [%s], Timestamp [%s]",
        consumerRecord.key(), consumerRecord.topic(),
        consumerRecord.partition(), consumerRecord.offset(),
        consumerRecord.timestamp());

      log.error(message, exception);
    }
  }

}