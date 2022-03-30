package com.codecafe.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class RetryTopicConfig {

  private final KafkaTemplate<Object, Object> template;

  private final ConsumerFactory<String, String> consumerFactory;

  public RetryTopicConfig(KafkaTemplate<Object, Object> template, ConsumerFactory<String, String> consumerFactory) {
    this.template = template;
    this.consumerFactory = consumerFactory;
  }


  //Container Factory containing bocking error handler
  @Bean
  ConcurrentKafkaListenerContainerFactory<String, String> kafkaBlockingRetryContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setCommonErrorHandler(retryErrorHandler());
    return factory;
  }

  // This is a blocking retry (will move offset only when all tries are completed)
  // DeadLetterPublishingRecoverer publishes message to DLT when all retries are over
  public DefaultErrorHandler retryErrorHandler() {
    return new DefaultErrorHandler(new DeadLetterPublishingRecoverer(template), new FixedBackOff(2000, 3));
  }

}