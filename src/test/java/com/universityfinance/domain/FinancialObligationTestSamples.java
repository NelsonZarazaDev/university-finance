package com.universityfinance.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FinancialObligationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static FinancialObligation getFinancialObligationSample1() {
        return new FinancialObligation().id(1L);
    }

    public static FinancialObligation getFinancialObligationSample2() {
        return new FinancialObligation().id(2L);
    }

    public static FinancialObligation getFinancialObligationRandomSampleGenerator() {
        return new FinancialObligation().id(longCount.incrementAndGet());
    }
}
