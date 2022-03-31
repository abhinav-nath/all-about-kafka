package com.codecafe.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import static java.time.LocalDateTime.now;

@Slf4j
@Configuration
public class RetryTopicConfig {

  private final KafkaTemplate<Object, Object> kafkaTemplate;

  private final ConsumerFactory<String, String> consumerFactory;

  public RetryTopicConfig(KafkaTemplate<Object, Object> kafkaTemplate, ConsumerFactory<String, String> consumerFactory) {
    this.kafkaTemplate = kafkaTemplate;
    this.consumerFactory = consumerFactory;
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<String, String> localRetryContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setCommonErrorHandler(localRetryHandler());
    return factory;
  }

  public DefaultErrorHandler localRetryHandler() {
    return new DefaultErrorHandler(
      (consumerRecord, exception) -> {
        log.info("localRetryHandler Received message with key : [{}], value : [{}], topic : [{}], at : [{}]",
          consumerRecord.key(),
          consumerRecord.value(),
          consumerRecord.topic(),
          now());

        //kafkaTemplate.send("products-retry-0", consumerRecord.key(), consumerRecord.value());
      }
      , new FixedBackOff(2000, 3));
  }

}