package com.universityfinance.web.rest;

import static com.universityfinance.domain.FinancialObligationAsserts.*;
import static com.universityfinance.web.rest.TestUtil.createUpdateProxyForBean;
import static com.universityfinance.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.universityfinance.IntegrationTest;
import com.universityfinance.domain.FinancialObligation;
import com.universityfinance.domain.Student;
import com.universityfinance.domain.enumeration.ObligationStatus;
import com.universityfinance.repository.FinancialObligationRepository;
import com.universityfinance.service.dto.FinancialObligationDTO;
import com.universityfinance.service.mapper.FinancialObligationMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link FinancialObligationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FinancialObligationResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.parse("2026-11-21");

    private static final ObligationStatus DEFAULT_STATUS = ObligationStatus.OUTSTANDING;
    private static final ObligationStatus UPDATED_STATUS = ObligationStatus.PARTIALLY_PAID;

    private static final String ENTITY_API_URL = "/api/financial-obligations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FinancialObligationRepository financialObligationRepository;

    @Autowired
    private FinancialObligationMapper financialObligationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFinancialObligationMockMvc;

    private FinancialObligation financialObligation;

    private FinancialObligation insertedFinancialObligation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialObligation createEntity(EntityManager em) {
        FinancialObligation financialObligation = new FinancialObligation()
            .amount(DEFAULT_AMOUNT)
            .dueDate(DEFAULT_DUE_DATE)
            .status(DEFAULT_STATUS);
        // Add required entity
        Student student;
        if (TestUtil.findAll(em, Student.class).isEmpty()) {
            student = StudentResourceIT.createEntity();
            em.persist(student);
            em.flush();
        } else {
            student = TestUtil.findAll(em, Student.class).getFirst();
        }
        financialObligation.setStudent(student);
        return financialObligation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialObligation createUpdatedEntity(EntityManager em) {
        FinancialObligation updatedFinancialObligation = new FinancialObligation()
            .amount(UPDATED_AMOUNT)
            .dueDate(UPDATED_DUE_DATE)
            .status(UPDATED_STATUS);
        // Add required entity
        Student student;
        if (TestUtil.findAll(em, Student.class).isEmpty()) {
            student = StudentResourceIT.createUpdatedEntity();
            em.persist(student);
            em.flush();
        } else {
            student = TestUtil.findAll(em, Student.class).getFirst();
        }
        updatedFinancialObligation.setStudent(student);
        return updatedFinancialObligation;
    }

    @BeforeEach
    void initTest() {
        financialObligation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFinancialObligation != null) {
            financialObligationRepository.delete(insertedFinancialObligation);
            insertedFinancialObligation = null;
        }
    }

    @Test
    @Transactional
    void createFinancialObligation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FinancialObligation
        FinancialObligationDTO financialObligationDTO = financialObligationMapper.toDto(financialObligation);
        var returnedFinancialObligationDTO = om.readValue(
            restFinancialObligationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financialObligationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FinancialObligationDTO.class
        );

        // Validate the FinancialObligation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFinancialObligation = financialObligationMapper.toEntity(returnedFinancialObligationDTO);
        assertFinancialObligationUpdatableFieldsEquals(
            returnedFinancialObligation,
            getPersistedFinancialObligation(returnedFinancialObligation)
        );

        insertedFinancialObligation = returnedFinancialObligation;
    }

    @Test
    @Transactional
    void createFinancialObligationWithExistingId() throws Exception {
        // Create the FinancialObligation with an existing ID
        financialObligation.setId(1L);
        FinancialObligationDTO financialObligationDTO = financialObligationMapper.toDto(financialObligation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinancialObligationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financialObligationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FinancialObligation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financialObligation.setAmount(null);

        // Create the FinancialObligation, which fails.
        FinancialObligationDTO financialObligationDTO = financialObligationMapper.toDto(financialObligation);

        restFinancialObligationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financialObligationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financialObligation.setDueDate(null);

        // Create the FinancialObligation, which fails.
        FinancialObligationDTO financialObligationDTO = financialObligationMapper.toDto(financialObligation);

        restFinancialObligationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financialObligationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financialObligation.setStatus(null);

        // Create the FinancialObligation, which fails.
        FinancialObligationDTO financialObligationDTO = financialObligationMapper.toDto(financialObligation);

        restFinancialObligationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financialObligationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFinancialObligations() throws Exception {
        // Initialize the database
        insertedFinancialObligation = financialObligationRepository.saveAndFlush(financialObligation);

        // Get all the financialObligationList
        restFinancialObligationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financialObligation.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getFinancialObligation() throws Exception {
        // Initialize the database
        insertedFinancialObligation = financialObligationRepository.saveAndFlush(financialObligation);

        // Get the financialObligation
        restFinancialObligationMockMvc
            .perform(get(ENTITY_API_URL_ID, financialObligation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(financialObligation.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFinancialObligation() throws Exception {
        // Get the financialObligation
        restFinancialObligationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFinancialObligation() throws Exception {
        // Initialize the database
        insertedFinancialObligation = financialObligationRepository.saveAndFlush(financialObligation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financialObligation
        FinancialObligation updatedFinancialObligation = financialObligationRepository.findById(financialObligation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFinancialObligation are not directly saved in db
        em.detach(updatedFinancialObligation);
        updatedFinancialObligation.amount(UPDATED_AMOUNT).dueDate(UPDATED_DUE_DATE).status(UPDATED_STATUS);
        FinancialObligationDTO financialObligationDTO = financialObligationMapper.toDto(updatedFinancialObligation);

        restFinancialObligationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financialObligationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialObligationDTO))
            )
            .andExpect(status().isOk());

        // Validate the FinancialObligation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFinancialObligationToMatchAllProperties(updatedFinancialObligation);
    }

    @Test
    @Transactional
    void putNonExistingFinancialObligation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialObligation.setId(longCount.incrementAndGet());

        // Create the FinancialObligation
        FinancialObligationDTO financialObligationDTO = financialObligationMapper.toDto(financialObligation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancialObligationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financialObligationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialObligationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialObligation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFinancialObligation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialObligation.setId(longCount.incrementAndGet());

        // Create the FinancialObligation
        FinancialObligationDTO financialObligationDTO = financialObligationMapper.toDto(financialObligation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialObligationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialObligationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialObligation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFinancialObligation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialObligation.setId(longCount.incrementAndGet());

        // Create the FinancialObligation
        FinancialObligationDTO financialObligationDTO = financialObligationMapper.toDto(financialObligation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialObligationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financialObligationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinancialObligation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFinancialObligationWithPatch() throws Exception {
        // Initialize the database
        insertedFinancialObligation = financialObligationRepository.saveAndFlush(financialObligation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financialObligation using partial update
        FinancialObligation partialUpdatedFinancialObligation = new FinancialObligation();
        partialUpdatedFinancialObligation.setId(financialObligation.getId());

        partialUpdatedFinancialObligation.dueDate(UPDATED_DUE_DATE).status(UPDATED_STATUS);

        restFinancialObligationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancialObligation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinancialObligation))
            )
            .andExpect(status().isOk());

        // Validate the FinancialObligation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinancialObligationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFinancialObligation, financialObligation),
            getPersistedFinancialObligation(financialObligation)
        );
    }

    @Test
    @Transactional
    void fullUpdateFinancialObligationWithPatch() throws Exception {
        // Initialize the database
        insertedFinancialObligation = financialObligationRepository.saveAndFlush(financialObligation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financialObligation using partial update
        FinancialObligation partialUpdatedFinancialObligation = new FinancialObligation();
        partialUpdatedFinancialObligation.setId(financialObligation.getId());

        partialUpdatedFinancialObligation.amount(UPDATED_AMOUNT).dueDate(UPDATED_DUE_DATE).status(UPDATED_STATUS);

        restFinancialObligationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancialObligation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinancialObligation))
            )
            .andExpect(status().isOk());

        // Validate the FinancialObligation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinancialObligationUpdatableFieldsEquals(
            partialUpdatedFinancialObligation,
            getPersistedFinancialObligation(partialUpdatedFinancialObligation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFinancialObligation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialObligation.setId(longCount.incrementAndGet());

        // Create the FinancialObligation
        FinancialObligationDTO financialObligationDTO = financialObligationMapper.toDto(financialObligation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancialObligationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, financialObligationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financialObligationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialObligation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFinancialObligation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialObligation.setId(longCount.incrementAndGet());

        // Create the FinancialObligation
        FinancialObligationDTO financialObligationDTO = financialObligationMapper.toDto(financialObligation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialObligationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financialObligationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialObligation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFinancialObligation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialObligation.setId(longCount.incrementAndGet());

        // Create the FinancialObligation
        FinancialObligationDTO financialObligationDTO = financialObligationMapper.toDto(financialObligation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialObligationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(financialObligationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinancialObligation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFinancialObligation() throws Exception {
        // Initialize the database
        insertedFinancialObligation = financialObligationRepository.saveAndFlush(financialObligation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the financialObligation
        restFinancialObligationMockMvc
            .perform(delete(ENTITY_API_URL_ID, financialObligation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return financialObligationRepository.count();
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

    protected FinancialObligation getPersistedFinancialObligation(FinancialObligation financialObligation) {
        return financialObligationRepository.findById(financialObligation.getId()).orElseThrow();
    }

    protected void assertPersistedFinancialObligationToMatchAllProperties(FinancialObligation expectedFinancialObligation) {
        assertFinancialObligationAllPropertiesEquals(
            expectedFinancialObligation,
            getPersistedFinancialObligation(expectedFinancialObligation)
        );
    }

    protected void assertPersistedFinancialObligationToMatchUpdatableProperties(FinancialObligation expectedFinancialObligation) {
        assertFinancialObligationAllUpdatablePropertiesEquals(
            expectedFinancialObligation,
            getPersistedFinancialObligation(expectedFinancialObligation)
        );
    }
}
