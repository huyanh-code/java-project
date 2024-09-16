package com.bookstore.service;

import com.bookstore.domain.*; // for static metamodels
import com.bookstore.domain.OtherEntity;
import com.bookstore.repository.OtherEntityRepository;
import com.bookstore.service.criteria.OtherEntityCriteria;
import com.bookstore.service.dto.OtherEntityDTO;
import com.bookstore.service.mapper.OtherEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OtherEntity} entities in the database.
 * The main input is a {@link OtherEntityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OtherEntityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OtherEntityQueryService extends QueryService<OtherEntity> {

    private static final Logger LOG = LoggerFactory.getLogger(OtherEntityQueryService.class);

    private final OtherEntityRepository otherEntityRepository;

    private final OtherEntityMapper otherEntityMapper;

    public OtherEntityQueryService(OtherEntityRepository otherEntityRepository, OtherEntityMapper otherEntityMapper) {
        this.otherEntityRepository = otherEntityRepository;
        this.otherEntityMapper = otherEntityMapper;
    }

    /**
     * Return a {@link Page} of {@link OtherEntityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OtherEntityDTO> findByCriteria(OtherEntityCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OtherEntity> specification = createSpecification(criteria);
        return otherEntityRepository.findAll(specification, page).map(otherEntityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OtherEntityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OtherEntity> specification = createSpecification(criteria);
        return otherEntityRepository.count(specification);
    }

    /**
     * Function to convert {@link OtherEntityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OtherEntity> createSpecification(OtherEntityCriteria criteria) {
        Specification<OtherEntity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OtherEntity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OtherEntity_.name));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), OtherEntity_.birthDate));
            }
        }
        return specification;
    }
}
