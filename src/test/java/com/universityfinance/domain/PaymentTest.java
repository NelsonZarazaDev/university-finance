package com.universityfinance.domain;

import static com.universityfinance.domain.FinancialObligationTestSamples.*;
import static com.universityfinance.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.universityfinance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void financialObligationTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        FinancialObligation financialObligationBack = getFinancialObligationRandomSampleGenerator();

        payment.setFinancialObligation(financialObligationBack);
        assertThat(payment.getFinancialObligation()).isEqualTo(financialObligationBack);

        payment.financialObligation(null);
        assertThat(payment.getFinancialObligation()).isNull();
    }
}
