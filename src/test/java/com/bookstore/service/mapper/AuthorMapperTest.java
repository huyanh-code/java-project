package com.bookstore.service.mapper;

import static com.bookstore.domain.AuthorAsserts.*;
import static com.bookstore.domain.AuthorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthorMapperTest {

    private AuthorMapper authorMapper;

    @BeforeEach
    void setUp() {
        authorMapper = new AuthorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAuthorSample1();
        var actual = authorMapper.toEntity(authorMapper.toDto(expected));
        assertAuthorAllPropertiesEquals(expected, actual);
    }
}
