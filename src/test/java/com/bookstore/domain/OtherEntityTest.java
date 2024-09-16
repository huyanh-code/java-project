package com.bookstore.domain;

import static com.bookstore.domain.OtherEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bookstore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OtherEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OtherEntity.class);
        OtherEntity otherEntity1 = getOtherEntitySample1();
        OtherEntity otherEntity2 = new OtherEntity();
        assertThat(otherEntity1).isNotEqualTo(otherEntity2);

        otherEntity2.setId(otherEntity1.getId());
        assertThat(otherEntity1).isEqualTo(otherEntity2);

        otherEntity2 = getOtherEntitySample2();
        assertThat(otherEntity1).isNotEqualTo(otherEntity2);
    }
}
