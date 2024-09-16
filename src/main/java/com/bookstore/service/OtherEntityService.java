package com.bookstore.service;

import com.bookstore.service.dto.OtherEntityDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bookstore.domain.OtherEntity}.
 */
public interface OtherEntityService {
    /**
     * Save a otherEntity.
     *
     * @param otherEntityDTO the entity to save.
     * @return the persisted entity.
     */
    OtherEntityDTO save(OtherEntityDTO otherEntityDTO);

    /**
     * Updates a otherEntity.
     *
     * @param otherEntityDTO the entity to update.
     * @return the persisted entity.
     */
    OtherEntityDTO update(OtherEntityDTO otherEntityDTO);

    /**
     * Partially updates a otherEntity.
     *
     * @param otherEntityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OtherEntityDTO> partialUpdate(OtherEntityDTO otherEntityDTO);

    /**
     * Get the "id" otherEntity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OtherEntityDTO> findOne(Long id);

    /**
     * Delete the "id" otherEntity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
