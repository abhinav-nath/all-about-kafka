package com.codecafe.kafka.avro.config;

import com.codecafe.kafka.avro.error.ConsumerErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.listener.LoggingErrorHandler;

@EnableKafka
@Configuration
public class ConsumerConfig {

  @Bean
  public LoggingErrorHandler errorHandler() {
    return new ConsumerErrorHandler();
  }

}
