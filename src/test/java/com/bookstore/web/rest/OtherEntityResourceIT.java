package com.bookstore.web.rest;

import static com.bookstore.domain.OtherEntityAsserts.*;
import static com.bookstore.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bookstore.IntegrationTest;
import com.bookstore.domain.OtherEntity;
import com.bookstore.repository.OtherEntityRepository;
import com.bookstore.service.dto.OtherEntityDTO;
import com.bookstore.service.mapper.OtherEntityMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link OtherEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OtherEntityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/other-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OtherEntityRepository otherEntityRepository;

    @Autowired
    private OtherEntityMapper otherEntityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOtherEntityMockMvc;

    private OtherEntity otherEntity;

    private OtherEntity insertedOtherEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OtherEntity createEntity() {
        return new OtherEntity().name(DEFAULT_NAME).birthDate(DEFAULT_BIRTH_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OtherEntity createUpdatedEntity() {
        return new OtherEntity().name(UPDATED_NAME).birthDate(UPDATED_BIRTH_DATE);
    }

    @BeforeEach
    public void initTest() {
        otherEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOtherEntity != null) {
            otherEntityRepository.delete(insertedOtherEntity);
            insertedOtherEntity = null;
        }
    }

    @Test
    @Transactional
    void createOtherEntity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OtherEntity
        OtherEntityDTO otherEntityDTO = otherEntityMapper.toDto(otherEntity);
        var returnedOtherEntityDTO = om.readValue(
            restOtherEntityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otherEntityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OtherEntityDTO.class
        );

        // Validate the OtherEntity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOtherEntity = otherEntityMapper.toEntity(returnedOtherEntityDTO);
        assertOtherEntityUpdatableFieldsEquals(returnedOtherEntity, getPersistedOtherEntity(returnedOtherEntity));

        insertedOtherEntity = returnedOtherEntity;
    }

    @Test
    @Transactional
    void createOtherEntityWithExistingId() throws Exception {
        // Create the OtherEntity with an existing ID
        otherEntity.setId(1L);
        OtherEntityDTO otherEntityDTO = otherEntityMapper.toDto(otherEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOtherEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otherEntityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OtherEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        otherEntity.setName(null);

        // Create the OtherEntity, which fails.
        OtherEntityDTO otherEntityDTO = otherEntityMapper.toDto(otherEntity);

        restOtherEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otherEntityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        otherEntity.setBirthDate(null);

        // Create the OtherEntity, which fails.
        OtherEntityDTO otherEntityDTO = otherEntityMapper.toDto(otherEntity);

        restOtherEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otherEntityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOtherEntities() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get all the otherEntityList
        restOtherEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(otherEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));
    }

    @Test
    @Transactional
    void getOtherEntity() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get the otherEntity
        restOtherEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, otherEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(otherEntity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()));
    }

    @Test
    @Transactional
    void getOtherEntitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        Long id = otherEntity.getId();

        defaultOtherEntityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOtherEntityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOtherEntityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOtherEntitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get all the otherEntityList where name equals to
        defaultOtherEntityFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOtherEntitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get all the otherEntityList where name in
        defaultOtherEntityFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOtherEntitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get all the otherEntityList where name is not null
        defaultOtherEntityFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllOtherEntitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get all the otherEntityList where name contains
        defaultOtherEntityFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOtherEntitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get all the otherEntityList where name does not contain
        defaultOtherEntityFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllOtherEntitiesByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get all the otherEntityList where birthDate equals to
        defaultOtherEntityFiltering("birthDate.equals=" + DEFAULT_BIRTH_DATE, "birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllOtherEntitiesByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get all the otherEntityList where birthDate in
        defaultOtherEntityFiltering("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE, "birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllOtherEntitiesByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get all the otherEntityList where birthDate is not null
        defaultOtherEntityFiltering("birthDate.specified=true", "birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOtherEntitiesByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get all the otherEntityList where birthDate is greater than or equal to
        defaultOtherEntityFiltering(
            "birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE,
            "birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE
        );
    }

    @Test
    @Transactional
    void getAllOtherEntitiesByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get all the otherEntityList where birthDate is less than or equal to
        defaultOtherEntityFiltering("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE, "birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllOtherEntitiesByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get all the otherEntityList where birthDate is less than
        defaultOtherEntityFiltering("birthDate.lessThan=" + UPDATED_BIRTH_DATE, "birthDate.lessThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllOtherEntitiesByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        // Get all the otherEntityList where birthDate is greater than
        defaultOtherEntityFiltering("birthDate.greaterThan=" + SMALLER_BIRTH_DATE, "birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);
    }

    private void defaultOtherEntityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOtherEntityShouldBeFound(shouldBeFound);
        defaultOtherEntityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOtherEntityShouldBeFound(String filter) throws Exception {
        restOtherEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(otherEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));

        // Check, that the count call also returns 1
        restOtherEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOtherEntityShouldNotBeFound(String filter) throws Exception {
        restOtherEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOtherEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOtherEntity() throws Exception {
        // Get the otherEntity
        restOtherEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOtherEntity() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the otherEntity
        OtherEntity updatedOtherEntity = otherEntityRepository.findById(otherEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOtherEntity are not directly saved in db
        em.detach(updatedOtherEntity);
        updatedOtherEntity.name(UPDATED_NAME).birthDate(UPDATED_BIRTH_DATE);
        OtherEntityDTO otherEntityDTO = otherEntityMapper.toDto(updatedOtherEntity);

        restOtherEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, otherEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(otherEntityDTO))
            )
            .andExpect(status().isOk());

        // Validate the OtherEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOtherEntityToMatchAllProperties(updatedOtherEntity);
    }

    @Test
    @Transactional
    void putNonExistingOtherEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otherEntity.setId(longCount.incrementAndGet());

        // Create the OtherEntity
        OtherEntityDTO otherEntityDTO = otherEntityMapper.toDto(otherEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtherEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, otherEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(otherEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtherEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOtherEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otherEntity.setId(longCount.incrementAndGet());

        // Create the OtherEntity
        OtherEntityDTO otherEntityDTO = otherEntityMapper.toDto(otherEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtherEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(otherEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtherEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOtherEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otherEntity.setId(longCount.incrementAndGet());

        // Create the OtherEntity
        OtherEntityDTO otherEntityDTO = otherEntityMapper.toDto(otherEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtherEntityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otherEntityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OtherEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOtherEntityWithPatch() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the otherEntity using partial update
        OtherEntity partialUpdatedOtherEntity = new OtherEntity();
        partialUpdatedOtherEntity.setId(otherEntity.getId());

        partialUpdatedOtherEntity.name(UPDATED_NAME).birthDate(UPDATED_BIRTH_DATE);

        restOtherEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtherEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOtherEntity))
            )
            .andExpect(status().isOk());

        // Validate the OtherEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOtherEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOtherEntity, otherEntity),
            getPersistedOtherEntity(otherEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateOtherEntityWithPatch() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the otherEntity using partial update
        OtherEntity partialUpdatedOtherEntity = new OtherEntity();
        partialUpdatedOtherEntity.setId(otherEntity.getId());

        partialUpdatedOtherEntity.name(UPDATED_NAME).birthDate(UPDATED_BIRTH_DATE);

        restOtherEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtherEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOtherEntity))
            )
            .andExpect(status().isOk());

        // Validate the OtherEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOtherEntityUpdatableFieldsEquals(partialUpdatedOtherEntity, getPersistedOtherEntity(partialUpdatedOtherEntity));
    }

    @Test
    @Transactional
    void patchNonExistingOtherEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otherEntity.setId(longCount.incrementAndGet());

        // Create the OtherEntity
        OtherEntityDTO otherEntityDTO = otherEntityMapper.toDto(otherEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtherEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, otherEntityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(otherEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtherEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOtherEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otherEntity.setId(longCount.incrementAndGet());

        // Create the OtherEntity
        OtherEntityDTO otherEntityDTO = otherEntityMapper.toDto(otherEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtherEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(otherEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtherEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOtherEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otherEntity.setId(longCount.incrementAndGet());

        // Create the OtherEntity
        OtherEntityDTO otherEntityDTO = otherEntityMapper.toDto(otherEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtherEntityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(otherEntityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OtherEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOtherEntity() throws Exception {
        // Initialize the database
        insertedOtherEntity = otherEntityRepository.saveAndFlush(otherEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the otherEntity
        restOtherEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, otherEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return otherEntityRepository.count();
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

    protected OtherEntity getPersistedOtherEntity(OtherEntity otherEntity) {
        return otherEntityRepository.findById(otherEntity.getId()).orElseThrow();
    }

    protected void assertPersistedOtherEntityToMatchAllProperties(OtherEntity expectedOtherEntity) {
        assertOtherEntityAllPropertiesEquals(expectedOtherEntity, getPersistedOtherEntity(expectedOtherEntity));
    }

    protected void assertPersistedOtherEntityToMatchUpdatableProperties(OtherEntity expectedOtherEntity) {
        assertOtherEntityAllUpdatablePropertiesEquals(expectedOtherEntity, getPersistedOtherEntity(expectedOtherEntity));
    }
}
