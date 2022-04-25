package com.codecafe.kafka.avro.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.retrytopic.DefaultDestinationTopicResolver;
import org.springframework.kafka.retrytopic.DestinationTopicResolver;
import org.springframework.kafka.retrytopic.RetryTopicInternalBeanNames;

import static java.time.Clock.systemUTC;
import static java.util.Collections.emptyMap;

@EnableKafka
@Configuration
public class ConsumerConfig {

  @Bean(name = RetryTopicInternalBeanNames.DESTINATION_TOPIC_CONTAINER_NAME)
  public DestinationTopicResolver destinationTopicResolver(ApplicationContext context) {
    DefaultDestinationTopicResolver resolver = new DefaultDestinationTopicResolver(systemUTC(), context);
    resolver.setClassifications(emptyMap(), true);
    return resolver;
  }

}