package com.bookstore.web.rest;

import com.bookstore.repository.AuthorRepository;
import com.bookstore.service.AuthorQueryService;
import com.bookstore.service.AuthorService;
import com.bookstore.service.criteria.AuthorCriteria;
import com.bookstore.service.dto.AuthorDTO;
import com.bookstore.web.rest.errors.BadRequestAlertException;
import jakarta.servlet.ServletContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bookstore.domain.Author}.
 */
@RestController
@RequestMapping("/api/authors")
public class AuthorResource {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorResource.class);

    private static final String ENTITY_NAME = "author";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthorService authorService;

    private final AuthorRepository authorRepository;

    private final AuthorQueryService authorQueryService;

    @Autowired
    private ServletContext context;

    public AuthorResource(AuthorService authorService, AuthorRepository authorRepository, AuthorQueryService authorQueryService) {
        this.authorService = authorService;
        this.authorRepository = authorRepository;
        this.authorQueryService = authorQueryService;
    }

    /**
     * {@code POST  /authors} : Create a new author.
     *
     * @param authorDTO the authorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new authorDTO, or with status {@code 400 (Bad Request)} if the author has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AuthorDTO> createAuthor(@ModelAttribute AuthorDTO authorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Author : {}", authorDTO);

        // Kiểm tra nếu authorDTO đã có ID
        if (authorDTO.getId() != null) {
            throw new BadRequestAlertException("A new author cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Log thông tin hình ảnh nếu có
        if (authorDTO.getImage() != null) {
            LOG.info("Image file: {}, size: {}", authorDTO.getImage().getOriginalFilename(), authorDTO.getImage().getSize());
        }

        // Lưu tác giả và hình ảnh
        authorDTO = authorService.save(authorDTO, context.getRealPath(""));

        return ResponseEntity.created(new URI("/api/authors/" + authorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, authorDTO.getId().toString()))
            .body(authorDTO);
    }

    /**
     * {@code PUT  /authors/:id} : Updates an existing author.
     *
     * @param id the id of the authorDTO to save.
     * @param authorDTO the authorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorDTO,
     * or with status {@code 400 (Bad Request)} if the authorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping(path = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AuthorDTO> updateAuthor(
        @PathVariable(value = "id", required = false) final Long id,
        @ModelAttribute AuthorDTO authorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Author : {}, {}", id, authorDTO);
        if (authorDTO.getImage() != null) {
            LOG.info("Image file: {}, size: {}", authorDTO.getImage().getOriginalFilename(), authorDTO.getImage().getSize());
        }
        if (authorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        authorDTO = authorService.update(authorDTO, context.getRealPath(""));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorDTO.getId().toString()))
            .body(authorDTO);
    }

    /**
     * {@code PATCH  /authors/:id} : Partial updates given fields of an existing author, field will ignore if it is null
     *
     * @param id the id of the authorDTO to save.
     * @param authorDTO the authorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorDTO,
     * or with status {@code 400 (Bad Request)} if the authorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the authorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the authorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AuthorDTO> partialUpdateAuthor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AuthorDTO authorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Author partially : {}, {}", id, authorDTO);
        if (authorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AuthorDTO> result = authorService.partialUpdate(authorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /authors} : get all the authors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AuthorDTO>> getAllAuthors(
        AuthorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Authors by criteria: {}", criteria);

        Page<AuthorDTO> page = authorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /authors/count} : count all the authors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAuthors(AuthorCriteria criteria) {
        LOG.debug("REST request to count Authors by criteria: {}", criteria);
        return ResponseEntity.ok().body(authorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /authors/:id} : get the "id" author.
     *
     * @param id the id of the authorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Author : {}", id);
        Optional<AuthorDTO> authorDTO = authorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authorDTO);
    }

    /**
     * {@code DELETE  /authors/:id} : delete the "id" author.
     *
     * @param id the id of the authorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Author : {}", id);
        authorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
