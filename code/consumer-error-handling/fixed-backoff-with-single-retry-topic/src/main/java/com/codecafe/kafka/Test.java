package com.codecafe.kafka;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class Test {

    @Retryable(maxAttempts = 5)
    public int sum(int a, int b) {
      System.out.println("==> Entered inside sum method");
        if (a == 2)
            throw new RuntimeException("Error in method sum");
        return a + b;
    }

}
