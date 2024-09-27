package com.bookstore.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bookstore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageAuthorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageAuthorDTO.class);
        ImageAuthorDTO imageAuthorDTO1 = new ImageAuthorDTO();
        imageAuthorDTO1.setId(1L);
        ImageAuthorDTO imageAuthorDTO2 = new ImageAuthorDTO();
        assertThat(imageAuthorDTO1).isNotEqualTo(imageAuthorDTO2);
        imageAuthorDTO2.setId(imageAuthorDTO1.getId());
        assertThat(imageAuthorDTO1).isEqualTo(imageAuthorDTO2);
        imageAuthorDTO2.setId(2L);
        assertThat(imageAuthorDTO1).isNotEqualTo(imageAuthorDTO2);
        imageAuthorDTO1.setId(null);
        assertThat(imageAuthorDTO1).isNotEqualTo(imageAuthorDTO2);
    }
}
