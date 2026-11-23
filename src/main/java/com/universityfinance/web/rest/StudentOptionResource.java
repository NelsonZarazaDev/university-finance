package com.universityfinance.web.rest;

import com.universityfinance.service.StudentOptionService;
import com.universityfinance.service.dto.StudentOptionDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudentOptionResource {

    private final StudentOptionService studentOptionService;

    @GetMapping(path = "/api/student-options")
    public List<StudentOptionDTO> getStudentOptions() {
        return studentOptionService.getStudentOptions();
    }
}
