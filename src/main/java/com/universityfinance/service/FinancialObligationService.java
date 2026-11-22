package com.universityfinance.service;

import com.universityfinance.domain.FinancialObligation;
import com.universityfinance.repository.FinancialObligationRepository;
import com.universityfinance.service.dto.FinancialObligationDTO;
import com.universityfinance.service.mapper.FinancialObligationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.universityfinance.domain.FinancialObligation}.
 */
@Service
@Transactional
public class FinancialObligationService {

    private static final Logger LOG = LoggerFactory.getLogger(FinancialObligationService.class);

    private final FinancialObligationRepository financialObligationRepository;

    private final FinancialObligationMapper financialObligationMapper;

    public FinancialObligationService(
        FinancialObligationRepository financialObligationRepository,
        FinancialObligationMapper financialObligationMapper
    ) {
        this.financialObligationRepository = financialObligationRepository;
        this.financialObligationMapper = financialObligationMapper;
    }

    /**
     * Save a financialObligation.
     *
     * @param financialObligationDTO the entity to save.
     * @return the persisted entity.
     */
    public FinancialObligationDTO save(FinancialObligationDTO financialObligationDTO) {
        LOG.debug("Request to save FinancialObligation : {}", financialObligationDTO);
        FinancialObligation financialObligation = financialObligationMapper.toEntity(financialObligationDTO);
        financialObligation = financialObligationRepository.save(financialObligation);
        return financialObligationMapper.toDto(financialObligation);
    }

    /**
     * Update a financialObligation.
     *
     * @param financialObligationDTO the entity to save.
     * @return the persisted entity.
     */
    public FinancialObligationDTO update(FinancialObligationDTO financialObligationDTO) {
        LOG.debug("Request to update FinancialObligation : {}", financialObligationDTO);
        FinancialObligation financialObligation = financialObligationMapper.toEntity(financialObligationDTO);
        financialObligation = financialObligationRepository.save(financialObligation);
        return financialObligationMapper.toDto(financialObligation);
    }

    /**
     * Partially update a financialObligation.
     *
     * @param financialObligationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FinancialObligationDTO> partialUpdate(FinancialObligationDTO financialObligationDTO) {
        LOG.debug("Request to partially update FinancialObligation : {}", financialObligationDTO);

        return financialObligationRepository
            .findById(financialObligationDTO.getId())
            .map(existingFinancialObligation -> {
                financialObligationMapper.partialUpdate(existingFinancialObligation, financialObligationDTO);

                return existingFinancialObligation;
            })
            .map(financialObligationRepository::save)
            .map(financialObligationMapper::toDto);
    }

    /**
     * Get all the financialObligations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FinancialObligationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all FinancialObligations");
        return financialObligationRepository.findAll(pageable).map(financialObligationMapper::toDto);
    }

    /**
     * Get one financialObligation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FinancialObligationDTO> findOne(Long id) {
        LOG.debug("Request to get FinancialObligation : {}", id);
        return financialObligationRepository.findById(id).map(financialObligationMapper::toDto);
    }

    /**
     * Delete the financialObligation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FinancialObligation : {}", id);
        financialObligationRepository.deleteById(id);
    }
}
