package com.bookstore.web.rest;

import com.bookstore.repository.OtherEntityRepository;
import com.bookstore.service.OtherEntityQueryService;
import com.bookstore.service.OtherEntityService;
import com.bookstore.service.criteria.OtherEntityCriteria;
import com.bookstore.service.dto.OtherEntityDTO;
import com.bookstore.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.bookstore.domain.OtherEntity}.
 */
@RestController
@RequestMapping("/api/other-entities")
public class OtherEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(OtherEntityResource.class);

    private static final String ENTITY_NAME = "otherEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OtherEntityService otherEntityService;

    private final OtherEntityRepository otherEntityRepository;

    private final OtherEntityQueryService otherEntityQueryService;

    public OtherEntityResource(
        OtherEntityService otherEntityService,
        OtherEntityRepository otherEntityRepository,
        OtherEntityQueryService otherEntityQueryService
    ) {
        this.otherEntityService = otherEntityService;
        this.otherEntityRepository = otherEntityRepository;
        this.otherEntityQueryService = otherEntityQueryService;
    }

    /**
     * {@code POST  /other-entities} : Create a new otherEntity.
     *
     * @param otherEntityDTO the otherEntityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new otherEntityDTO, or with status {@code 400 (Bad Request)} if the otherEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OtherEntityDTO> createOtherEntity(@Valid @RequestBody OtherEntityDTO otherEntityDTO) throws URISyntaxException {
        LOG.debug("REST request to save OtherEntity : {}", otherEntityDTO);
        if (otherEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new otherEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        otherEntityDTO = otherEntityService.save(otherEntityDTO);
        return ResponseEntity.created(new URI("/api/other-entities/" + otherEntityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, otherEntityDTO.getId().toString()))
            .body(otherEntityDTO);
    }

    /**
     * {@code PUT  /other-entities/:id} : Updates an existing otherEntity.
     *
     * @param id the id of the otherEntityDTO to save.
     * @param otherEntityDTO the otherEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otherEntityDTO,
     * or with status {@code 400 (Bad Request)} if the otherEntityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the otherEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OtherEntityDTO> updateOtherEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OtherEntityDTO otherEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OtherEntity : {}, {}", id, otherEntityDTO);
        if (otherEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otherEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otherEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        otherEntityDTO = otherEntityService.update(otherEntityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otherEntityDTO.getId().toString()))
            .body(otherEntityDTO);
    }

    /**
     * {@code PATCH  /other-entities/:id} : Partial updates given fields of an existing otherEntity, field will ignore if it is null
     *
     * @param id the id of the otherEntityDTO to save.
     * @param otherEntityDTO the otherEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otherEntityDTO,
     * or with status {@code 400 (Bad Request)} if the otherEntityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the otherEntityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the otherEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OtherEntityDTO> partialUpdateOtherEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OtherEntityDTO otherEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OtherEntity partially : {}, {}", id, otherEntityDTO);
        if (otherEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otherEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otherEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OtherEntityDTO> result = otherEntityService.partialUpdate(otherEntityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otherEntityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /other-entities} : get all the otherEntities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of otherEntities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OtherEntityDTO>> getAllOtherEntities(
        OtherEntityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OtherEntities by criteria: {}", criteria);

        Page<OtherEntityDTO> page = otherEntityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /other-entities/count} : count all the otherEntities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOtherEntities(OtherEntityCriteria criteria) {
        LOG.debug("REST request to count OtherEntities by criteria: {}", criteria);
        return ResponseEntity.ok().body(otherEntityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /other-entities/:id} : get the "id" otherEntity.
     *
     * @param id the id of the otherEntityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the otherEntityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OtherEntityDTO> getOtherEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OtherEntity : {}", id);
        Optional<OtherEntityDTO> otherEntityDTO = otherEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(otherEntityDTO);
    }

    /**
     * {@code DELETE  /other-entities/:id} : delete the "id" otherEntity.
     *
     * @param id the id of the otherEntityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOtherEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OtherEntity : {}", id);
        otherEntityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
