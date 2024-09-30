package com.bookstore.repository;

import com.bookstore.domain.ImageAuthor;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImageAuthor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageAuthorRepository extends JpaRepository<ImageAuthor, Long>, JpaSpecificationExecutor<ImageAuthor> {
    List<ImageAuthor> findByAuthorId(Long authorId);
}
