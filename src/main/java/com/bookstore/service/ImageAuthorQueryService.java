package com.bookstore.service;

import com.bookstore.domain.*; // for static metamodels
import com.bookstore.domain.ImageAuthor;
import com.bookstore.repository.ImageAuthorRepository;
import com.bookstore.service.criteria.ImageAuthorCriteria;
import com.bookstore.service.dto.ImageAuthorDTO;
import com.bookstore.service.mapper.ImageAuthorMapper;
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
 * Service for executing complex queries for {@link ImageAuthor} entities in the database.
 * The main input is a {@link ImageAuthorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ImageAuthorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImageAuthorQueryService extends QueryService<ImageAuthor> {

    private static final Logger LOG = LoggerFactory.getLogger(ImageAuthorQueryService.class);

    private final ImageAuthorRepository imageAuthorRepository;

    private final ImageAuthorMapper imageAuthorMapper;

    public ImageAuthorQueryService(ImageAuthorRepository imageAuthorRepository, ImageAuthorMapper imageAuthorMapper) {
        this.imageAuthorRepository = imageAuthorRepository;
        this.imageAuthorMapper = imageAuthorMapper;
    }

    /**
     * Return a {@link Page} of {@link ImageAuthorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageAuthorDTO> findByCriteria(ImageAuthorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ImageAuthor> specification = createSpecification(criteria);
        return imageAuthorRepository.findAll(specification, page).map(imageAuthorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImageAuthorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ImageAuthor> specification = createSpecification(criteria);
        return imageAuthorRepository.count(specification);
    }

    /**
     * Function to convert {@link ImageAuthorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ImageAuthor> createSpecification(ImageAuthorCriteria criteria) {
        Specification<ImageAuthor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ImageAuthor_.id));
            }
            if (criteria.getAuthor_id() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAuthor_id(), root -> root.join(ImageAuthor_.author, JoinType.LEFT).get(Author_.id))
                );
            }
        }
        return specification;
    }
}
