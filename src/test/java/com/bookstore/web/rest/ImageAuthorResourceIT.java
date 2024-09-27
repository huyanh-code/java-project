package com.bookstore.web.rest;

import static com.bookstore.domain.ImageAuthorAsserts.*;
import static com.bookstore.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bookstore.IntegrationTest;
import com.bookstore.domain.Author;
import com.bookstore.domain.ImageAuthor;
import com.bookstore.repository.ImageAuthorRepository;
import com.bookstore.service.dto.ImageAuthorDTO;
import com.bookstore.service.mapper.ImageAuthorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ImageAuthorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageAuthorResourceIT {

    private static final byte[] DEFAULT_IMAGE_AUTHOR = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_AUTHOR = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_AUTHOR_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_AUTHOR_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/image-authors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImageAuthorRepository imageAuthorRepository;

    @Autowired
    private ImageAuthorMapper imageAuthorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageAuthorMockMvc;

    private ImageAuthor imageAuthor;

    private ImageAuthor insertedImageAuthor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageAuthor createEntity() {
        return new ImageAuthor().imageAuthor(DEFAULT_IMAGE_AUTHOR).imageAuthorContentType(DEFAULT_IMAGE_AUTHOR_CONTENT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageAuthor createUpdatedEntity() {
        return new ImageAuthor().imageAuthor(UPDATED_IMAGE_AUTHOR).imageAuthorContentType(UPDATED_IMAGE_AUTHOR_CONTENT_TYPE);
    }

    @BeforeEach
    public void initTest() {
        imageAuthor = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedImageAuthor != null) {
            imageAuthorRepository.delete(insertedImageAuthor);
            insertedImageAuthor = null;
        }
    }

    @Test
    @Transactional
    void createImageAuthor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ImageAuthor
        ImageAuthorDTO imageAuthorDTO = imageAuthorMapper.toDto(imageAuthor);
        var returnedImageAuthorDTO = om.readValue(
            restImageAuthorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageAuthorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImageAuthorDTO.class
        );

        // Validate the ImageAuthor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImageAuthor = imageAuthorMapper.toEntity(returnedImageAuthorDTO);
        assertImageAuthorUpdatableFieldsEquals(returnedImageAuthor, getPersistedImageAuthor(returnedImageAuthor));

        insertedImageAuthor = returnedImageAuthor;
    }

    @Test
    @Transactional
    void createImageAuthorWithExistingId() throws Exception {
        // Create the ImageAuthor with an existing ID
        imageAuthor.setId(1L);
        ImageAuthorDTO imageAuthorDTO = imageAuthorMapper.toDto(imageAuthor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageAuthorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageAuthorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ImageAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImageAuthors() throws Exception {
        // Initialize the database
        insertedImageAuthor = imageAuthorRepository.saveAndFlush(imageAuthor);

        // Get all the imageAuthorList
        restImageAuthorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageAuthor.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageAuthorContentType").value(hasItem(DEFAULT_IMAGE_AUTHOR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageAuthor").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IMAGE_AUTHOR))));
    }

    @Test
    @Transactional
    void getImageAuthor() throws Exception {
        // Initialize the database
        insertedImageAuthor = imageAuthorRepository.saveAndFlush(imageAuthor);

        // Get the imageAuthor
        restImageAuthorMockMvc
            .perform(get(ENTITY_API_URL_ID, imageAuthor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imageAuthor.getId().intValue()))
            .andExpect(jsonPath("$.imageAuthorContentType").value(DEFAULT_IMAGE_AUTHOR_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageAuthor").value(Base64.getEncoder().encodeToString(DEFAULT_IMAGE_AUTHOR)));
    }

    @Test
    @Transactional
    void getImageAuthorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedImageAuthor = imageAuthorRepository.saveAndFlush(imageAuthor);

        Long id = imageAuthor.getId();

        defaultImageAuthorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultImageAuthorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultImageAuthorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImageAuthorsByAuthor_idIsEqualToSomething() throws Exception {
        Author author_id;
        if (TestUtil.findAll(em, Author.class).isEmpty()) {
            imageAuthorRepository.saveAndFlush(imageAuthor);
            author_id = AuthorResourceIT.createEntity();
        } else {
            author_id = TestUtil.findAll(em, Author.class).get(0);
        }
        em.persist(author_id);
        em.flush();
        imageAuthor.setAuthor(author_id);
        imageAuthorRepository.saveAndFlush(imageAuthor);
        Long author_idId = author_id.getId();
        // Get all the imageAuthorList where author_id equals to author_idId
        defaultImageAuthorShouldBeFound("author_idId.equals=" + author_idId);

        // Get all the imageAuthorList where author_id equals to (author_idId + 1)
        defaultImageAuthorShouldNotBeFound("author_idId.equals=" + (author_idId + 1));
    }

    private void defaultImageAuthorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultImageAuthorShouldBeFound(shouldBeFound);
        defaultImageAuthorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImageAuthorShouldBeFound(String filter) throws Exception {
        restImageAuthorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageAuthor.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageAuthorContentType").value(hasItem(DEFAULT_IMAGE_AUTHOR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageAuthor").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IMAGE_AUTHOR))));

        // Check, that the count call also returns 1
        restImageAuthorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImageAuthorShouldNotBeFound(String filter) throws Exception {
        restImageAuthorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImageAuthorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImageAuthor() throws Exception {
        // Get the imageAuthor
        restImageAuthorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImageAuthor() throws Exception {
        // Initialize the database
        insertedImageAuthor = imageAuthorRepository.saveAndFlush(imageAuthor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageAuthor
        ImageAuthor updatedImageAuthor = imageAuthorRepository.findById(imageAuthor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedImageAuthor are not directly saved in db
        em.detach(updatedImageAuthor);
        updatedImageAuthor.imageAuthor(UPDATED_IMAGE_AUTHOR).imageAuthorContentType(UPDATED_IMAGE_AUTHOR_CONTENT_TYPE);
        ImageAuthorDTO imageAuthorDTO = imageAuthorMapper.toDto(updatedImageAuthor);

        restImageAuthorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageAuthorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageAuthorDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImageAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImageAuthorToMatchAllProperties(updatedImageAuthor);
    }

    @Test
    @Transactional
    void putNonExistingImageAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageAuthor.setId(longCount.incrementAndGet());

        // Create the ImageAuthor
        ImageAuthorDTO imageAuthorDTO = imageAuthorMapper.toDto(imageAuthor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageAuthorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageAuthorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageAuthorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImageAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageAuthor.setId(longCount.incrementAndGet());

        // Create the ImageAuthor
        ImageAuthorDTO imageAuthorDTO = imageAuthorMapper.toDto(imageAuthor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageAuthorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageAuthorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImageAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageAuthor.setId(longCount.incrementAndGet());

        // Create the ImageAuthor
        ImageAuthorDTO imageAuthorDTO = imageAuthorMapper.toDto(imageAuthor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageAuthorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageAuthorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImageAuthorWithPatch() throws Exception {
        // Initialize the database
        insertedImageAuthor = imageAuthorRepository.saveAndFlush(imageAuthor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageAuthor using partial update
        ImageAuthor partialUpdatedImageAuthor = new ImageAuthor();
        partialUpdatedImageAuthor.setId(imageAuthor.getId());

        restImageAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageAuthor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImageAuthor))
            )
            .andExpect(status().isOk());

        // Validate the ImageAuthor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageAuthorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedImageAuthor, imageAuthor),
            getPersistedImageAuthor(imageAuthor)
        );
    }

    @Test
    @Transactional
    void fullUpdateImageAuthorWithPatch() throws Exception {
        // Initialize the database
        insertedImageAuthor = imageAuthorRepository.saveAndFlush(imageAuthor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageAuthor using partial update
        ImageAuthor partialUpdatedImageAuthor = new ImageAuthor();
        partialUpdatedImageAuthor.setId(imageAuthor.getId());

        partialUpdatedImageAuthor.imageAuthor(UPDATED_IMAGE_AUTHOR).imageAuthorContentType(UPDATED_IMAGE_AUTHOR_CONTENT_TYPE);

        restImageAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageAuthor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImageAuthor))
            )
            .andExpect(status().isOk());

        // Validate the ImageAuthor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageAuthorUpdatableFieldsEquals(partialUpdatedImageAuthor, getPersistedImageAuthor(partialUpdatedImageAuthor));
    }

    @Test
    @Transactional
    void patchNonExistingImageAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageAuthor.setId(longCount.incrementAndGet());

        // Create the ImageAuthor
        ImageAuthorDTO imageAuthorDTO = imageAuthorMapper.toDto(imageAuthor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageAuthorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageAuthorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImageAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageAuthor.setId(longCount.incrementAndGet());

        // Create the ImageAuthor
        ImageAuthorDTO imageAuthorDTO = imageAuthorMapper.toDto(imageAuthor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageAuthorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImageAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageAuthor.setId(longCount.incrementAndGet());

        // Create the ImageAuthor
        ImageAuthorDTO imageAuthorDTO = imageAuthorMapper.toDto(imageAuthor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageAuthorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(imageAuthorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageAuthor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImageAuthor() throws Exception {
        // Initialize the database
        insertedImageAuthor = imageAuthorRepository.saveAndFlush(imageAuthor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the imageAuthor
        restImageAuthorMockMvc
            .perform(delete(ENTITY_API_URL_ID, imageAuthor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return imageAuthorRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ImageAuthor getPersistedImageAuthor(ImageAuthor imageAuthor) {
        return imageAuthorRepository.findById(imageAuthor.getId()).orElseThrow();
    }

    protected void assertPersistedImageAuthorToMatchAllProperties(ImageAuthor expectedImageAuthor) {
        assertImageAuthorAllPropertiesEquals(expectedImageAuthor, getPersistedImageAuthor(expectedImageAuthor));
    }

    protected void assertPersistedImageAuthorToMatchUpdatableProperties(ImageAuthor expectedImageAuthor) {
        assertImageAuthorAllUpdatablePropertiesEquals(expectedImageAuthor, getPersistedImageAuthor(expectedImageAuthor));
    }
}
