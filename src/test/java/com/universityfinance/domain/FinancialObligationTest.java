package com.universityfinance.domain;

import static com.universityfinance.domain.FinancialObligationTestSamples.*;
import static com.universityfinance.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.universityfinance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinancialObligationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinancialObligation.class);
        FinancialObligation financialObligation1 = getFinancialObligationSample1();
        FinancialObligation financialObligation2 = new FinancialObligation();
        assertThat(financialObligation1).isNotEqualTo(financialObligation2);

        financialObligation2.setId(financialObligation1.getId());
        assertThat(financialObligation1).isEqualTo(financialObligation2);

        financialObligation2 = getFinancialObligationSample2();
        assertThat(financialObligation1).isNotEqualTo(financialObligation2);
    }

    @Test
    void studentTest() {
        FinancialObligation financialObligation = getFinancialObligationRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        financialObligation.setStudent(studentBack);
        assertThat(financialObligation.getStudent()).isEqualTo(studentBack);

        financialObligation.student(null);
        assertThat(financialObligation.getStudent()).isNull();
    }
}
