package com.bookstore.domain;

import static com.bookstore.domain.AuthorTestSamples.*;
import static com.bookstore.domain.ImageAuthorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bookstore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageAuthorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageAuthor.class);
        ImageAuthor imageAuthor1 = getImageAuthorSample1();
        ImageAuthor imageAuthor2 = new ImageAuthor();
        assertThat(imageAuthor1).isNotEqualTo(imageAuthor2);

        imageAuthor2.setId(imageAuthor1.getId());
        assertThat(imageAuthor1).isEqualTo(imageAuthor2);

        imageAuthor2 = getImageAuthorSample2();
        assertThat(imageAuthor1).isNotEqualTo(imageAuthor2);
    }

    @Test
    void author_idTest() {
        ImageAuthor imageAuthor = getImageAuthorRandomSampleGenerator();
        Author authorBack = getAuthorRandomSampleGenerator();

        imageAuthor.setAuthor(authorBack);
        assertThat(imageAuthor.getAuthor()).isEqualTo(authorBack);

        imageAuthor.author(null);
        assertThat(imageAuthor.getAuthor()).isNull();
    }
}
