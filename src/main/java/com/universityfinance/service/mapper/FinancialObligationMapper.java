package com.universityfinance.service.mapper;

import com.universityfinance.domain.FinancialObligation;
import com.universityfinance.domain.Student;
import com.universityfinance.service.dto.FinancialObligationDTO;
import com.universityfinance.service.dto.StudentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FinancialObligation} and its DTO {@link FinancialObligationDTO}.
 */
@Mapper(componentModel = "spring")
public interface FinancialObligationMapper extends EntityMapper<FinancialObligationDTO, FinancialObligation> {
    @Mapping(target = "student", source = "student", qualifiedByName = "studentId")
    FinancialObligationDTO toDto(FinancialObligation s);

    @Named("studentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    StudentDTO toDtoStudentId(Student student);
}
