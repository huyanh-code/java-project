package com.bookstore.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bookstore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OtherEntityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OtherEntityDTO.class);
        OtherEntityDTO otherEntityDTO1 = new OtherEntityDTO();
        otherEntityDTO1.setId(1L);
        OtherEntityDTO otherEntityDTO2 = new OtherEntityDTO();
        assertThat(otherEntityDTO1).isNotEqualTo(otherEntityDTO2);
        otherEntityDTO2.setId(otherEntityDTO1.getId());
        assertThat(otherEntityDTO1).isEqualTo(otherEntityDTO2);
        otherEntityDTO2.setId(2L);
        assertThat(otherEntityDTO1).isNotEqualTo(otherEntityDTO2);
        otherEntityDTO1.setId(null);
        assertThat(otherEntityDTO1).isNotEqualTo(otherEntityDTO2);
    }
}
