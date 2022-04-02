package com.codecafe.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("kafka")
@SpringBootTest
class RetryableDemoTest {

  @Autowired
  private RetryableDemo retryableDemo;

  @Test
  void testSum() {
    System.out.println(retryableDemo.sum(4, 4));
  }

}