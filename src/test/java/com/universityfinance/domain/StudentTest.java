package com.universityfinance.domain;

import static com.universityfinance.domain.FinancialObligationTestSamples.*;
import static com.universityfinance.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.universityfinance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StudentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Student.class);
        Student student1 = getStudentSample1();
        Student student2 = new Student();
        assertThat(student1).isNotEqualTo(student2);

        student2.setId(student1.getId());
        assertThat(student1).isEqualTo(student2);

        student2 = getStudentSample2();
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    void financialObligationTest() {
        Student student = getStudentRandomSampleGenerator();
        FinancialObligation financialObligationBack = getFinancialObligationRandomSampleGenerator();

        student.addFinancialObligation(financialObligationBack);
        assertThat(student.getFinancialObligations()).containsOnly(financialObligationBack);
        assertThat(financialObligationBack.getStudent()).isEqualTo(student);

        student.removeFinancialObligation(financialObligationBack);
        assertThat(student.getFinancialObligations()).doesNotContain(financialObligationBack);
        assertThat(financialObligationBack.getStudent()).isNull();

        student.financialObligations(new HashSet<>(Set.of(financialObligationBack)));
        assertThat(student.getFinancialObligations()).containsOnly(financialObligationBack);
        assertThat(financialObligationBack.getStudent()).isEqualTo(student);

        student.setFinancialObligations(new HashSet<>());
        assertThat(student.getFinancialObligations()).doesNotContain(financialObligationBack);
        assertThat(financialObligationBack.getStudent()).isNull();
    }
}
