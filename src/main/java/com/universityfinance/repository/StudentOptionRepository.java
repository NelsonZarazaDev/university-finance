package com.universityfinance.repository;

import com.universityfinance.domain.Student;
import com.universityfinance.repository.projection.StudentOptionView;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface StudentOptionRepository extends Repository<Student, Long> {
    List<StudentOptionView> findAllProjectedBy();
}
