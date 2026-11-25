package com.universityfinance.web.rest.errors;

import com.universityfinance.service.exception.PaymentValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler(PaymentValidationException.class)
    public ResponseEntity<String> handlePaymentValidation(PaymentValidationException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
