package com.bookstore.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OtherEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OtherEntity getOtherEntitySample1() {
        return new OtherEntity().id(1L).name("name1");
    }

    public static OtherEntity getOtherEntitySample2() {
        return new OtherEntity().id(2L).name("name2");
    }

    public static OtherEntity getOtherEntityRandomSampleGenerator() {
        return new OtherEntity().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
