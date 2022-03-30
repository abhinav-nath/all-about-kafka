package com.codecafe.kafka.config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.DestinationTopic;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.retrytopic.RetryTopicNamesProviderFactory;
import org.springframework.kafka.retrytopic.SuffixingRetryTopicNamesProviderFactory;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;

import java.util.List;

//@Configuration
public class RetryTopicConfig {

  //@Bean
  public RetryTopicConfiguration myRetryTopic(KafkaTemplate<String, String> kafkaTemplate) {
    return RetryTopicConfigurationBuilder
      .newInstance()
      .exponentialBackoff(1000, 2.0, 1000)
      .maxAttempts(4)
      .doNotAutoCreateRetryTopics()
      .includeTopics(List.of("products-retry"))
      .setTopicSuffixingStrategy(TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
      .create(kafkaTemplate);
  }

  //@Bean
  public RetryTopicNamesProviderFactory myRetryNamingProviderFactory() {
    return new CustomRetryTopicNamesProviderFactory();
  }

}

class CustomRetryTopicNamesProviderFactory implements RetryTopicNamesProviderFactory {

  @Override
  public RetryTopicNamesProvider createRetryTopicNamesProvider(DestinationTopic.Properties properties) {

    if (properties.isDltTopic()) {
      return new SuffixingRetryTopicNamesProviderFactory.SuffixingRetryTopicNamesProvider(properties) {

        @Override
        public String getTopicName(String topic) {
          return "products-dead-letter-topic";
        }

      };
    }

    if (properties.isMainEndpoint()) {
      return new SuffixingRetryTopicNamesProviderFactory.SuffixingRetryTopicNamesProvider(properties) {

        @Override
        public String getTopicName(String topic) {
          return "products";
        }

      };
    }

    return new SuffixingRetryTopicNamesProviderFactory.SuffixingRetryTopicNamesProvider(properties) {

      @Override
      public String getTopicName(String topic) {
        return "products-retry-topic";
      }

    };
  }

}