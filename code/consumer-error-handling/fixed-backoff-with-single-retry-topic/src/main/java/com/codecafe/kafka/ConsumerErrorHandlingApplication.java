package com.codecafe.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class ConsumerErrorHandlingApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConsumerErrorHandlingApplication.class, args);
  }

}
