package com.universityfinance.service;

import com.universityfinance.domain.Student;
import com.universityfinance.repository.StudentRepository;
import com.universityfinance.service.dto.StudentDTO;
import com.universityfinance.service.mapper.StudentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.universityfinance.domain.Student}.
 */
@Service
@Transactional
public class StudentService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    /**
     * Save a student.
     *
     * @param studentDTO the entity to save.
     * @return the persisted entity.
     */
    public StudentDTO save(StudentDTO studentDTO) {
        LOG.debug("Request to save Student : {}", studentDTO);
        Student student = studentMapper.toEntity(studentDTO);
        student = studentRepository.save(student);
        return studentMapper.toDto(student);
    }

    /**
     * Update a student.
     *
     * @param studentDTO the entity to save.
     * @return the persisted entity.
     */
    public StudentDTO update(StudentDTO studentDTO) {
        LOG.debug("Request to update Student : {}", studentDTO);
        Student student = studentMapper.toEntity(studentDTO);
        student = studentRepository.save(student);
        return studentMapper.toDto(student);
    }

    /**
     * Partially update a student.
     *
     * @param studentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StudentDTO> partialUpdate(StudentDTO studentDTO) {
        LOG.debug("Request to partially update Student : {}", studentDTO);

        return studentRepository
            .findById(studentDTO.getId())
            .map(existingStudent -> {
                studentMapper.partialUpdate(existingStudent, studentDTO);

                return existingStudent;
            })
            .map(studentRepository::save)
            .map(studentMapper::toDto);
    }

    /**
     * Get all the students.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Students");
        return studentRepository.findAll(pageable).map(studentMapper::toDto);
    }

    /**
     * Get one student by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudentDTO> findOne(Long id) {
        LOG.debug("Request to get Student : {}", id);
        return studentRepository.findById(id).map(studentMapper::toDto);
    }

    /**
     * Delete the student by id.
     *
     * @param id the id of the entity.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(Long id) {
        LOG.debug("Request to delete Student : {}", id);
        studentRepository.deleteById(id);
    }
}
