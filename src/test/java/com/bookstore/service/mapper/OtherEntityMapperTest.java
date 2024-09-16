package com.bookstore.service.mapper;

import static com.bookstore.domain.OtherEntityAsserts.*;
import static com.bookstore.domain.OtherEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OtherEntityMapperTest {

    private OtherEntityMapper otherEntityMapper;

    @BeforeEach
    void setUp() {
        otherEntityMapper = new OtherEntityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOtherEntitySample1();
        var actual = otherEntityMapper.toEntity(otherEntityMapper.toDto(expected));
        assertOtherEntityAllPropertiesEquals(expected, actual);
    }
}
