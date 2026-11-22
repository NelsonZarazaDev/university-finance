package com.universityfinance.service.mapper;

import static com.universityfinance.domain.FinancialObligationAsserts.*;
import static com.universityfinance.domain.FinancialObligationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FinancialObligationMapperTest {

    private FinancialObligationMapper financialObligationMapper;

    @BeforeEach
    void setUp() {
        financialObligationMapper = new FinancialObligationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFinancialObligationSample1();
        var actual = financialObligationMapper.toEntity(financialObligationMapper.toDto(expected));
        assertFinancialObligationAllPropertiesEquals(expected, actual);
    }
}
