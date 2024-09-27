package com.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ImageAuthor.
 */
@Entity
@Table(name = "image_author")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageAuthor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "image_author")
    private byte[] imageAuthor;

    @Column(name = "image_author_content_type")
    private String imageAuthorContentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "authors", "books" }, allowSetters = true)
    private Author author;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImageAuthor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageAuthor() {
        return this.imageAuthor;
    }

    public ImageAuthor imageAuthor(byte[] imageAuthor) {
        this.setImageAuthor(imageAuthor);
        return this;
    }

    public void setImageAuthor(byte[] imageAuthor) {
        this.imageAuthor = imageAuthor;
    }

    public String getImageAuthorContentType() {
        return this.imageAuthorContentType;
    }

    public ImageAuthor imageAuthorContentType(String imageAuthorContentType) {
        this.imageAuthorContentType = imageAuthorContentType;
        return this;
    }

    public void setImageAuthorContentType(String imageAuthorContentType) {
        this.imageAuthorContentType = imageAuthorContentType;
    }

    public Author getAuthor() {
        return this.author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public ImageAuthor author_id(Author author) {
        this.setAuthor(author);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageAuthor)) {
            return false;
        }
        return getId() != null && getId().equals(((ImageAuthor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageAuthor{" +
            "id=" + getId() +
            ", imageAuthor='" + getImageAuthor() + "'" +
            ", imageAuthorContentType='" + getImageAuthorContentType() + "'" +
            "}";
    }
}
