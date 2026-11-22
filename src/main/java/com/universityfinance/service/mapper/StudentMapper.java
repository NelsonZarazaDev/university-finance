package com.universityfinance.service.mapper;

import com.universityfinance.domain.Student;
import com.universityfinance.service.dto.StudentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Student} and its DTO {@link StudentDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentMapper extends EntityMapper<StudentDTO, Student> {}
