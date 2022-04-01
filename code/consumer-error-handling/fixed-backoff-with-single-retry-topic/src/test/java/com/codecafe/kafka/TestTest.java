package com.codecafe.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("kafka")
@SpringBootTest
class TestTest {

  @Autowired
  private Test test;

  @org.junit.jupiter.api.Test
  public void testSum() {
    System.out.println(test.sum(4, 4));
  }

}