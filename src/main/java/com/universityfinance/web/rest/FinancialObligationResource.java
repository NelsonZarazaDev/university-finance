package com.universityfinance.web.rest;

import com.universityfinance.repository.FinancialObligationRepository;
import com.universityfinance.service.FinancialObligationService;
import com.universityfinance.service.dto.FinancialObligationDTO;
import com.universityfinance.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.universityfinance.domain.FinancialObligation}.
 */
@RestController
@RequestMapping("/api/financial-obligations")
public class FinancialObligationResource {

    private static final Logger LOG = LoggerFactory.getLogger(FinancialObligationResource.class);

    private static final String ENTITY_NAME = "financialObligation";

    @Value("${jhipster.clientApp.name:universityFinance}")
    private String applicationName;

    private final FinancialObligationService financialObligationService;

    private final FinancialObligationRepository financialObligationRepository;

    public FinancialObligationResource(
        FinancialObligationService financialObligationService,
        FinancialObligationRepository financialObligationRepository
    ) {
        this.financialObligationService = financialObligationService;
        this.financialObligationRepository = financialObligationRepository;
    }

    /**
     * {@code POST  /financial-obligations} : Create a new financialObligation.
     *
     * @param financialObligationDTO the financialObligationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new financialObligationDTO, or with status {@code 400 (Bad Request)} if the financialObligation already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FinancialObligationDTO> createFinancialObligation(
        @Valid @RequestBody FinancialObligationDTO financialObligationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save FinancialObligation : {}", financialObligationDTO);
        if (financialObligationDTO.getId() != null) {
            throw new BadRequestAlertException("A new financialObligation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        financialObligationDTO = financialObligationService.save(financialObligationDTO);
        return ResponseEntity.created(new URI("/api/financial-obligations/" + financialObligationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, financialObligationDTO.getId().toString()))
            .body(financialObligationDTO);
    }

    /**
     * {@code PUT  /financial-obligations/:id} : Updates an existing financialObligation.
     *
     * @param id the id of the financialObligationDTO to save.
     * @param financialObligationDTO the financialObligationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialObligationDTO,
     * or with status {@code 400 (Bad Request)} if the financialObligationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the financialObligationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FinancialObligationDTO> updateFinancialObligation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FinancialObligationDTO financialObligationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update FinancialObligation : {}, {}", id, financialObligationDTO);
        if (financialObligationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialObligationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financialObligationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        financialObligationDTO = financialObligationService.update(financialObligationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financialObligationDTO.getId().toString()))
            .body(financialObligationDTO);
    }

    /**
     * {@code PATCH  /financial-obligations/:id} : Partial updates given fields of an existing financialObligation, field will ignore if it is null
     *
     * @param id the id of the financialObligationDTO to save.
     * @param financialObligationDTO the financialObligationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialObligationDTO,
     * or with status {@code 400 (Bad Request)} if the financialObligationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the financialObligationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the financialObligationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FinancialObligationDTO> partialUpdateFinancialObligation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FinancialObligationDTO financialObligationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update FinancialObligation : {}, {}", id, financialObligationDTO);
        if (financialObligationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialObligationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financialObligationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FinancialObligationDTO> result = financialObligationService.partialUpdate(financialObligationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financialObligationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /financial-obligations} : get all the Financial Obligations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Financial Obligations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FinancialObligationDTO>> getAllFinancialObligations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of FinancialObligations");
        Page<FinancialObligationDTO> page = financialObligationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /financial-obligations/:id} : get the "id" financialObligation.
     *
     * @param id the id of the financialObligationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the financialObligationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FinancialObligationDTO> getFinancialObligation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FinancialObligation : {}", id);
        Optional<FinancialObligationDTO> financialObligationDTO = financialObligationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(financialObligationDTO);
    }

    /**
     * {@code DELETE  /financial-obligations/:id} : delete the "id" financialObligation.
     *
     * @param id the id of the financialObligationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialObligation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FinancialObligation : {}", id);
        financialObligationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
