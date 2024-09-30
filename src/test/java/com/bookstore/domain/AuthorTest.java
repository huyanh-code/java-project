package com.bookstore.domain;

import static com.bookstore.domain.AuthorTestSamples.*;
import static com.bookstore.domain.BookTestSamples.*;
import static com.bookstore.domain.ImageAuthorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bookstore.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AuthorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Author.class);
        Author author1 = getAuthorSample1();
        Author author2 = new Author();
        assertThat(author1).isNotEqualTo(author2);

        author2.setId(author1.getId());
        assertThat(author1).isEqualTo(author2);

        author2 = getAuthorSample2();
        assertThat(author1).isNotEqualTo(author2);
    }

    @Test
    void authorTest() {
        Author author = getAuthorRandomSampleGenerator();
        ImageAuthor imageAuthorBack = getImageAuthorRandomSampleGenerator();

        author.addAuthor(imageAuthorBack);
        assertThat(author.getImages()).containsOnly(imageAuthorBack);
        assertThat(imageAuthorBack.getAuthor()).isEqualTo(author);

        author.removeAuthor(imageAuthorBack);
        assertThat(author.getImages()).doesNotContain(imageAuthorBack);
        assertThat(imageAuthorBack.getAuthor()).isNull();

        author.authors(new HashSet<>(Set.of(imageAuthorBack)));
        assertThat(author.getImages()).containsOnly(imageAuthorBack);
        assertThat(imageAuthorBack.getAuthor()).isEqualTo(author);

        author.setImages(new HashSet<>());
        assertThat(author.getImages()).doesNotContain(imageAuthorBack);
        assertThat(imageAuthorBack.getAuthor()).isNull();
    }

    @Test
    void bookTest() {
        Author author = getAuthorRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        author.addBook(bookBack);
        assertThat(author.getBooks()).containsOnly(bookBack);
        assertThat(bookBack.getAuthor()).isEqualTo(author);

        author.removeBook(bookBack);
        assertThat(author.getBooks()).doesNotContain(bookBack);
        assertThat(bookBack.getAuthor()).isNull();

        author.books(new HashSet<>(Set.of(bookBack)));
        assertThat(author.getBooks()).containsOnly(bookBack);
        assertThat(bookBack.getAuthor()).isEqualTo(author);

        author.setBooks(new HashSet<>());
        assertThat(author.getBooks()).doesNotContain(bookBack);
        assertThat(bookBack.getAuthor()).isNull();
    }
}
