package com.universityfinance.domain;

import com.universityfinance.domain.enumeration.StudentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Student implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "student_code", length = 20, nullable = false, unique = true)
    private String studentCode;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(name = "first_name", length = 60, nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(name = "last_name", length = 60, nullable = false)
    private String lastName;

    @NotNull
    @Size(max = 120)
    @Column(name = "email", length = 120, nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StudentStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Student id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentCode() {
        return this.studentCode;
    }

    public Student studentCode(String studentCode) {
        this.setStudentCode(studentCode);
        return this;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Student firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Student lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Student email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getEnrollmentDate() {
        return this.enrollmentDate;
    }

    public Student enrollmentDate(LocalDate enrollmentDate) {
        this.setEnrollmentDate(enrollmentDate);
        return this;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public StudentStatus getStatus() {
        return this.status;
    }

    public Student status(StudentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return getId() != null && getId().equals(((Student) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", studentCode='" + getStudentCode() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", enrollmentDate='" + getEnrollmentDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
