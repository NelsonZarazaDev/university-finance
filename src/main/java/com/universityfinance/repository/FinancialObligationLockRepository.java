package com.universityfinance.repository;

import com.universityfinance.domain.FinancialObligation;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface FinancialObligationLockRepository extends Repository<FinancialObligation, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT fo FROM FinancialObligation fo WHERE fo.id = :id")
    Optional<FinancialObligation> findByIdForUpdate(@Param("id") Long id);
}
