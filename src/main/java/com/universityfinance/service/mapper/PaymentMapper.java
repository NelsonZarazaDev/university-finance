package com.universityfinance.service.mapper;

import com.universityfinance.domain.FinancialObligation;
import com.universityfinance.domain.Payment;
import com.universityfinance.service.dto.FinancialObligationDTO;
import com.universityfinance.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "financialObligation", source = "financialObligation", qualifiedByName = "financialObligationId")
    PaymentDTO toDto(Payment s);

    @Named("financialObligationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FinancialObligationDTO toDtoFinancialObligationId(FinancialObligation financialObligation);
}
