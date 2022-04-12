package com.codecafe.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.retrytopic.RetryTopicNamesProviderFactory;
import org.springframework.kafka.retrytopic.SuffixingRetryTopicNamesProviderFactory;

import static org.springframework.kafka.retrytopic.DestinationTopic.Properties;

@Configuration
public class ConsumerConfig {

  @Bean
  public RetryTopicNamesProviderFactory myRetryNamingProviderFactory() {
    return new CustomRetryTopicNamesProviderFactory();
  }

}

class CustomRetryTopicNamesProviderFactory implements RetryTopicNamesProviderFactory {

  @Override
  public RetryTopicNamesProvider createRetryTopicNamesProvider(Properties properties) {

    if (properties.isMainEndpoint()) {
      return new SuffixingRetryTopicNamesProviderFactory.SuffixingRetryTopicNamesProvider(properties);
    } else {
      return new SuffixingRetryTopicNamesProviderFactory.SuffixingRetryTopicNamesProvider(properties) {

        @Override
        public String getTopicName(String topic) {
          return "my-prefix-" + super.getTopicName(topic);
        }

      };
    }
  }

}