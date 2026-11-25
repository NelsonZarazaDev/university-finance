package com.universityfinance.repository;

import com.universityfinance.domain.Payment;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PaymentBalanceRepository extends Repository<Payment, Long> {
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.financialObligation.id = :obligationId")
    BigDecimal sumPaymentsByObligationId(@Param("obligationId") Long obligationId);
}
