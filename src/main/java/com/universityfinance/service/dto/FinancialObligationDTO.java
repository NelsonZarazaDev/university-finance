package com.universityfinance.service.dto;

import com.universityfinance.domain.enumeration.ObligationStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.universityfinance.domain.FinancialObligation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinancialObligationDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amount;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private ObligationStatus status;

    @NotNull
    private StudentDTO student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public ObligationStatus getStatus() {
        return status;
    }

    public void setStatus(ObligationStatus status) {
        this.status = status;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinancialObligationDTO)) {
            return false;
        }

        FinancialObligationDTO financialObligationDTO = (FinancialObligationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, financialObligationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinancialObligationDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", dueDate='" + getDueDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", student=" + getStudent() +
            "}";
    }
}
