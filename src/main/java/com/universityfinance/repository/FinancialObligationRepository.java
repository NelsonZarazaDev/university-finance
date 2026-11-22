package com.universityfinance.repository;

import com.universityfinance.domain.FinancialObligation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FinancialObligation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinancialObligationRepository extends JpaRepository<FinancialObligation, Long> {}
