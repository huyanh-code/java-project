package com.bookstore.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bookstore.domain.ImageAuthor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageAuthorDTO implements Serializable {

    private Long id;

    private String imageUrl;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    //    private AuthorDTO author_id;
    private Long authorId;
    private String authorName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    //    public AuthorDTO getAuthorId() {
    //        return authorId;
    //    }
    //
    //    public void setAuthorId(AuthorDTO authorId) {
    //        this.authorId = authorId;
    //    }

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
            ", imageUrl='" + getImageUrl() + "'" +
            ", authorId=" + getAuthorId() +
            ", authorName=" + getAuthorName() +
            "}";
    }
}
