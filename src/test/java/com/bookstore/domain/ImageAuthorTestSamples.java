package com.bookstore.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ImageAuthorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ImageAuthor getImageAuthorSample1() {
        return new ImageAuthor().id(1L);
    }

    public static ImageAuthor getImageAuthorSample2() {
        return new ImageAuthor().id(2L);
    }

    public static ImageAuthor getImageAuthorRandomSampleGenerator() {
        return new ImageAuthor().id(longCount.incrementAndGet());
    }
}
