package com.universityfinance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StudentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Student getStudentSample1() {
        return new Student().id(1L).studentCode("studentCode1").firstName("firstName1").lastName("lastName1").email("email1");
    }

    public static Student getStudentSample2() {
        return new Student().id(2L).studentCode("studentCode2").firstName("firstName2").lastName("lastName2").email("email2");
    }

    public static Student getStudentRandomSampleGenerator() {
        return new Student()
            .id(longCount.incrementAndGet())
            .studentCode(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
