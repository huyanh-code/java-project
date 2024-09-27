package com.bookstore.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bookstore.domain.ImageAuthor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageAuthorDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] imageAuthor;

    private String imageAuthorContentType;

    private AuthorDTO author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageAuthor() {
        return imageAuthor;
    }

    public void setImageAuthor(byte[] imageAuthor) {
        this.imageAuthor = imageAuthor;
    }

    public String getImageAuthorContentType() {
        return imageAuthorContentType;
    }

    public void setImageAuthorContentType(String imageAuthorContentType) {
        this.imageAuthorContentType = imageAuthorContentType;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageAuthorDTO)) {
            return false;
        }

        ImageAuthorDTO imageAuthorDTO = (ImageAuthorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imageAuthorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageAuthorDTO{" +
            "id=" + getId() +
            ", imageAuthor='" + getImageAuthor() + "'" +
            ", author_id=" + getAuthor() +
            "}";
    }
}
