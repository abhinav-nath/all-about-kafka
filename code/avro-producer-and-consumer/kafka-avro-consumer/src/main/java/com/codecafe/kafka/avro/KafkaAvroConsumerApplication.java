package com.codecafe.kafka.avro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class KafkaAvroConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(KafkaAvroConsumerApplication.class, args);
  }

}