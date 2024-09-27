package com.bookstore.service;

import com.bookstore.domain.*; // for static metamodels
import com.bookstore.domain.Author;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.service.criteria.AuthorCriteria;
import com.bookstore.service.dto.AuthorDTO;
import com.bookstore.service.mapper.AuthorMapper;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Author} entities in the database.
 * The main input is a {@link AuthorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AuthorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuthorQueryService extends QueryService<Author> {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorQueryService.class);

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    public AuthorQueryService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    /**
     * Return a {@link Page} of {@link AuthorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuthorDTO> findByCriteria(AuthorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Author> specification = createSpecification(criteria);
        var results = authorRepository.findAll(specification, page);
        //        var firstAuthor = results.getContent().stream().filter(a -> a.getId() == 1).findFirst();
        //        var firstAuthorImage = firstAuthor.get().getAuthors();

        return results.map(authorMapper::authorToAuthorDTO);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuthorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Author> specification = createSpecification(criteria);
        return authorRepository.count(specification);
    }

    /**
     * Function to convert {@link AuthorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Author> createSpecification(AuthorCriteria criteria) {
        Specification<Author> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Author_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Author_.name));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), Author_.birthDate));
            }
            //            if (criteria.getImageAuthor() != null) {
            //                specification = specification.and(
            //                    (root, query, builder) -> {
            //                        var join = root.join(Author_.authors, JoinType.LEFT);
            //
            //
            //                        buildSpecification()
            //                        return join.get
            //
            //                        return builder.equal(join.get(ImageAuthor_.id), root.getModel().getId());
            ////                        return builder.like(imageAuthorJoin.get(String.valueOf(ImageAuthor_.imageAuthor)), "%" + criteria.getImageAuthor().getContains() + "%");
            //                    }
            //                );
            //
            //            specification = specification.and(
            //                buildSpecification(criteria.getId(), root -> root.join(Author_.authors, JoinType.LEFT))
            //            );

            //                }
            if (criteria.getBookId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getBookId(), root -> root.join(Author_.books, JoinType.LEFT).get(Book_.id))
                );
            }
            //            if (criteria.getImageAuthorId() != null) {
            //                specification = specification.and(
            //                    buildSpecification(criteria.getImageAuthorId(),
            //                        root -> root.join(Author_.authors, JoinType.LEFT).get(ImageAuthor_.id))
            //                );
            //            }
        }
        return specification;
    }
}
