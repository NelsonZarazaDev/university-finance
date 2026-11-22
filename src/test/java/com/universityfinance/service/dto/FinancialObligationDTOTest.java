package com.universityfinance.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.universityfinance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinancialObligationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinancialObligationDTO.class);
        FinancialObligationDTO financialObligationDTO1 = new FinancialObligationDTO();
        financialObligationDTO1.setId(1L);
        FinancialObligationDTO financialObligationDTO2 = new FinancialObligationDTO();
        assertThat(financialObligationDTO1).isNotEqualTo(financialObligationDTO2);
        financialObligationDTO2.setId(financialObligationDTO1.getId());
        assertThat(financialObligationDTO1).isEqualTo(financialObligationDTO2);
        financialObligationDTO2.setId(2L);
        assertThat(financialObligationDTO1).isNotEqualTo(financialObligationDTO2);
        financialObligationDTO1.setId(null);
        assertThat(financialObligationDTO1).isNotEqualTo(financialObligationDTO2);
    }
}
