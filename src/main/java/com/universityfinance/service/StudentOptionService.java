package com.universityfinance.service;

import com.universityfinance.repository.StudentOptionRepository;
import com.universityfinance.service.dto.StudentOptionDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentOptionService {

    private final StudentOptionRepository studentOptionRepository;

    @Cacheable(cacheNames = "studentOptions")
    public List<StudentOptionDTO> getStudentOptions() {
        return studentOptionRepository
            .findAllProjectedBy()
            .stream()
            .map(student -> new StudentOptionDTO(student.getId(), student.getFirstName(), student.getLastName()))
            .toList();
    }
}
