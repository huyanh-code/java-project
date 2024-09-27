package com.bookstore.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bookstore.domain.ImageAuthor} entity. This class is used
 * in {@link com.bookstore.web.rest.ImageAuthorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /image-authors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageAuthorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter author_id;

    private Boolean distinct;

    public ImageAuthorCriteria() {}

    public ImageAuthorCriteria(ImageAuthorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.author_id = other.optionalAuthor_id().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ImageAuthorCriteria copy() {
        return new ImageAuthorCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getAuthor_id() {
        return author_id;
    }

    public Optional<LongFilter> optionalAuthor_id() {
        return Optional.ofNullable(author_id);
    }

    public LongFilter author_id() {
        if (author_id == null) {
            setAuthor_id(new LongFilter());
        }
        return author_id;
    }

    public void setAuthor_id(LongFilter author_id) {
        this.author_id = author_id;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ImageAuthorCriteria that = (ImageAuthorCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(author_id, that.author_id) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author_id, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageAuthorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAuthor_id().map(f -> "author_id=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
