package com.universityfinance.service;

import com.universityfinance.domain.FinancialObligation;
import com.universityfinance.domain.Payment;
import com.universityfinance.domain.enumeration.ObligationStatus;
import com.universityfinance.repository.FinancialObligationLockRepository;
import com.universityfinance.repository.FinancialObligationRepository;
import com.universityfinance.repository.PaymentBalanceRepository;
import com.universityfinance.repository.PaymentRepository;
import com.universityfinance.service.dto.CreatePaymentRequest;
import com.universityfinance.service.exception.PaymentValidationException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentOperationService {

    private final PaymentRepository paymentRepository;
    private final FinancialObligationRepository financialObligationRepository;
    private final FinancialObligationLockRepository financialObligationLockRepository;
    private final PaymentBalanceRepository paymentBalanceRepository;

    private FinancialObligation getLockedObligation(Long id) {
        return financialObligationLockRepository.findByIdForUpdate(id).orElseThrow();
    }

    private BigDecimal calculateOutstandingBalance(FinancialObligation financialObligation) {
        BigDecimal amount = financialObligation.getAmount();
        BigDecimal totalPaid = paymentBalanceRepository.sumPaymentsByObligationId(financialObligation.getId());
        return amount.subtract(totalPaid);
    }

    private void validatePaymentAmount(BigDecimal amount) {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El valor es null o igual 0");
        }
    }

    private void validatePaymentAgainstBalance(BigDecimal amount, BigDecimal outstandingBalance) {
        if (amount.compareTo(outstandingBalance) > 0) {
            throw new PaymentValidationException("Payment amount exceeds outstanding balance");
        }
    }

    public void registerPayment(CreatePaymentRequest createPaymentRequest) {
        validatePaymentAmount(createPaymentRequest.amount());
        FinancialObligation lockedObligation = getLockedObligation(createPaymentRequest.financialObligationId());
        BigDecimal outstandingBalance = calculateOutstandingBalance(lockedObligation);
        validatePaymentAgainstBalance(createPaymentRequest.amount(), outstandingBalance);

        Payment payment = new Payment();

        payment.setAmount(createPaymentRequest.amount());
        payment.setFinancialObligation(lockedObligation);
        payment.setPaymentDate(Instant.now());
        paymentRepository.save(payment);

        BigDecimal remainingBalance = outstandingBalance.subtract(createPaymentRequest.amount());

        ObligationStatus status = switch (remainingBalance.compareTo(BigDecimal.ZERO)) {
            case 0 -> ObligationStatus.PAID;
            case 1 -> ObligationStatus.PARTIALLY_PAID;
            default -> ObligationStatus.OUTSTANDING;
        };

        lockedObligation.setStatus(status);
    }
}
