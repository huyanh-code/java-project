package com.bookstore.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageAuthorAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertImageAuthorAllPropertiesEquals(ImageAuthor expected, ImageAuthor actual) {
        assertImageAuthorAutoGeneratedPropertiesEquals(expected, actual);
        assertImageAuthorAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertImageAuthorAllUpdatablePropertiesEquals(ImageAuthor expected, ImageAuthor actual) {
        assertImageAuthorUpdatableFieldsEquals(expected, actual);
        assertImageAuthorUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertImageAuthorAutoGeneratedPropertiesEquals(ImageAuthor expected, ImageAuthor actual) {
        assertThat(expected)
            .as("Verify ImageAuthor auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertImageAuthorUpdatableFieldsEquals(ImageAuthor expected, ImageAuthor actual) {
        assertThat(expected)
            .as("Verify ImageAuthor relevant properties")
            .satisfies(e -> assertThat(e.getImage_url()).as("check image_url").isEqualTo(actual.getImage_url()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertImageAuthorUpdatableRelationshipsEquals(ImageAuthor expected, ImageAuthor actual) {
        assertThat(expected)
            .as("Verify ImageAuthor relationships")
            .satisfies(e -> assertThat(e.getAuthor()).as("check author_id").isEqualTo(actual.getAuthor()));
    }
}
