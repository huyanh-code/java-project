package com.bookstore.repository;

import com.bookstore.domain.Author;
import com.bookstore.service.dto.AuthorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

//import java.awt.print.Pageable;

/**
 * Spring Data JPA repository for the Author entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    //    @EntityGraph(value = "authors", type = EntityGraph.EntityGraphType.FETCH)
    //    Page<Author> findAllWithImage(@Nullable Specification<Author> spec, Pageable pageable);
}
