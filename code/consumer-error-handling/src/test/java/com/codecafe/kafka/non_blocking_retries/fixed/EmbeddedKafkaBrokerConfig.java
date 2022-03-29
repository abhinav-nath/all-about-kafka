package com.codecafe.kafka.non_blocking_retries.fixed;

import kafka.server.KafkaConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

@PropertySource("application-kafka.properties")
public class EmbeddedKafkaBrokerConfig {

  @Value("${kafka.embedded.port}")
  private int embeddedKafkaBrokerPort;

  @Bean
  public EmbeddedKafkaBroker embeddedKafkaBroker() {
    return new EmbeddedKafkaBroker(
      1,
      false,
      1,
      "products",
      "products-retry",
      "products-dlt")
      .kafkaPorts(embeddedKafkaBrokerPort)
      .brokerProperty(KafkaConfig.AutoCreateTopicsEnableProp(), "false");
  }

}