package com.bookstore.web.rest;

import com.bookstore.repository.ImageAuthorRepository;
import com.bookstore.service.ImageAuthorQueryService;
import com.bookstore.service.ImageAuthorService;
import com.bookstore.service.criteria.ImageAuthorCriteria;
import com.bookstore.service.dto.ImageAuthorDTO;
import com.bookstore.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bookstore.domain.ImageAuthor}.
 */
@RestController
@RequestMapping("/api/image-authors")
public class ImageAuthorResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImageAuthorResource.class);

    private static final String ENTITY_NAME = "imageAuthor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageAuthorService imageAuthorService;

    private final ImageAuthorRepository imageAuthorRepository;

    private final ImageAuthorQueryService imageAuthorQueryService;

    public ImageAuthorResource(
        ImageAuthorService imageAuthorService,
        ImageAuthorRepository imageAuthorRepository,
        ImageAuthorQueryService imageAuthorQueryService
    ) {
        this.imageAuthorService = imageAuthorService;
        this.imageAuthorRepository = imageAuthorRepository;
        this.imageAuthorQueryService = imageAuthorQueryService;
    }

    /**
     * {@code POST  /image-authors} : Create a new imageAuthor.
     *
     * @param imageAuthorDTO the imageAuthorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageAuthorDTO, or with status {@code 400 (Bad Request)} if the imageAuthor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ImageAuthorDTO> createImageAuthor(@RequestBody ImageAuthorDTO imageAuthorDTO) throws URISyntaxException {
        LOG.debug("REST request to save ImageAuthor : {}", imageAuthorDTO);
        if (imageAuthorDTO.getId() != null) {
            throw new BadRequestAlertException("A new imageAuthor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        imageAuthorDTO = imageAuthorService.save(imageAuthorDTO);
        return ResponseEntity.created(new URI("/api/image-authors/" + imageAuthorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, imageAuthorDTO.getId().toString()))
            .body(imageAuthorDTO);
    }

    /**
     * {@code PUT  /image-authors/:id} : Updates an existing imageAuthor.
     *
     * @param id the id of the imageAuthorDTO to save.
     * @param imageAuthorDTO the imageAuthorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageAuthorDTO,
     * or with status {@code 400 (Bad Request)} if the imageAuthorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageAuthorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImageAuthorDTO> updateImageAuthor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImageAuthorDTO imageAuthorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ImageAuthor : {}, {}", id, imageAuthorDTO);
        if (imageAuthorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageAuthorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageAuthorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        imageAuthorDTO = imageAuthorService.update(imageAuthorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageAuthorDTO.getId().toString()))
            .body(imageAuthorDTO);
    }

    /**
     * {@code PATCH  /image-authors/:id} : Partial updates given fields of an existing imageAuthor, field will ignore if it is null
     *
     * @param id the id of the imageAuthorDTO to save.
     * @param imageAuthorDTO the imageAuthorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageAuthorDTO,
     * or with status {@code 400 (Bad Request)} if the imageAuthorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imageAuthorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageAuthorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImageAuthorDTO> partialUpdateImageAuthor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImageAuthorDTO imageAuthorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ImageAuthor partially : {}, {}", id, imageAuthorDTO);
        if (imageAuthorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageAuthorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageAuthorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImageAuthorDTO> result = imageAuthorService.partialUpdate(imageAuthorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageAuthorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /image-authors} : get all the imageAuthors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageAuthors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ImageAuthorDTO>> getAllImageAuthors(
        ImageAuthorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ImageAuthors by criteria: {}", criteria);

        Page<ImageAuthorDTO> page = imageAuthorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /image-authors/count} : count all the imageAuthors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countImageAuthors(ImageAuthorCriteria criteria) {
        LOG.debug("REST request to count ImageAuthors by criteria: {}", criteria);
        return ResponseEntity.ok().body(imageAuthorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /image-authors/:id} : get the "id" imageAuthor.
     *
     * @param id the id of the imageAuthorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageAuthorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImageAuthorDTO> getImageAuthor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ImageAuthor : {}", id);
        Optional<ImageAuthorDTO> imageAuthorDTO = imageAuthorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imageAuthorDTO);
    }

    /**
     * {@code DELETE  /image-authors/:id} : delete the "id" imageAuthor.
     *
     * @param id the id of the imageAuthorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImageAuthor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ImageAuthor : {}", id);
        imageAuthorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
