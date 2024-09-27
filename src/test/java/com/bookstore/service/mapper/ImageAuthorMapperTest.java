package com.bookstore.service.mapper;

import static com.bookstore.domain.ImageAuthorAsserts.*;
import static com.bookstore.domain.ImageAuthorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageAuthorMapperTest {

    private ImageAuthorMapper imageAuthorMapper;

    @BeforeEach
    void setUp() {
        imageAuthorMapper = new ImageAuthorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImageAuthorSample1();
        var actual = imageAuthorMapper.toEntity(imageAuthorMapper.toDto(expected));
        assertImageAuthorAllPropertiesEquals(expected, actual);
    }
}
