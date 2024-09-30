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

    private StringFilter image_url;

    private LongFilter author;

    private Boolean distinct;

    public ImageAuthorCriteria() {}

    public ImageAuthorCriteria(ImageAuthorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.image_url = other.optionalImage_url().map(StringFilter::copy).orElse(null);
        this.author = other.optionalAuthor().map(LongFilter::copy).orElse(null);
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

    public StringFilter getImage_url() {
        return image_url;
    }

    public Optional<StringFilter> optionalImage_url() {
        return Optional.ofNullable(image_url);
    }

    public StringFilter image_url() {
        if (image_url == null) {
            setImage_url(new StringFilter());
        }
        return image_url;
    }

    public void setImage_url(StringFilter image_url) {
        this.image_url = image_url;
    }

    public LongFilter getAuthor() {
        return author;
    }

    public Optional<LongFilter> optionalAuthor() {
        return Optional.ofNullable(author);
    }

    public LongFilter author() {
        if (author == null) {
            setAuthor(new LongFilter());
        }
        return author;
    }

    public void setAuthor(LongFilter author) {
        this.author = author;
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
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(image_url, that.image_url) &&
            Objects.equals(author, that.author) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image_url, author, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageAuthorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalImage_url().map(f -> "image_url=" + f + ", ").orElse("") +
            optionalAuthor().map(f -> "author_id=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
