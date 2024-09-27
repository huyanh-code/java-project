package com.bookstore.service;

import com.bookstore.service.dto.ImageAuthorDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bookstore.domain.ImageAuthor}.
 */
public interface ImageAuthorService {
    /**
     * Save a imageAuthor.
     *
     * @param imageAuthorDTO the entity to save.
     * @return the persisted entity.
     */
    ImageAuthorDTO save(ImageAuthorDTO imageAuthorDTO);

    /**
     * Updates a imageAuthor.
     *
     * @param imageAuthorDTO the entity to update.
     * @return the persisted entity.
     */
    ImageAuthorDTO update(ImageAuthorDTO imageAuthorDTO);

    /**
     * Partially updates a imageAuthor.
     *
     * @param imageAuthorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ImageAuthorDTO> partialUpdate(ImageAuthorDTO imageAuthorDTO);

    /**
     * Get the "id" imageAuthor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ImageAuthorDTO> findOne(Long id);

    /**
     * Delete the "id" imageAuthor.
     *
     * @param id the id of the entity.
     */

    void delete(Long id);
}
