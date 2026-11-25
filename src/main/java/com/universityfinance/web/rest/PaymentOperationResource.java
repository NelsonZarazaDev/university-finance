package com.universityfinance.web.rest;

import com.universityfinance.service.PaymentOperationService;
import com.universityfinance.service.dto.CreatePaymentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-operations")
@RequiredArgsConstructor
public class PaymentOperationResource {

    private final PaymentOperationService paymentOperationService;

    @PostMapping()
    public ResponseEntity<Void> registerPayment(@Valid @RequestBody CreatePaymentRequest createPaymentRequest) {
        paymentOperationService.registerPayment(createPaymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
