package com.smartcare.hospital_management.controller;

import com.smartcare.hospital_management.entity.Payment;
import com.smartcare.hospital_management.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }

    @PostMapping("/bill/{billId}")
    public ResponseEntity<Payment> recordPayment(
            @PathVariable Long billId,
            @RequestBody Payment payment
    ) {
        Payment saved =
                paymentService.recordPayment(
                        payment,
                        billId
                );

        return new ResponseEntity<>(
                saved,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{paymentId}")
    public Payment getPaymentById(
            @PathVariable Long paymentId
    ) {
        return paymentService
                .getPaymentById(paymentId);
    }

    @GetMapping("/bill/{billId}")
    public List<Payment> getPaymentsByBill(
            @PathVariable Long billId
    ) {
        return paymentService
                .getPaymentsByBill(billId);
    }
}
