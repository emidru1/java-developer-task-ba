package com.emidru1.payments.controller;

import com.emidru1.payments.dtos.PaymentDto;
import com.emidru1.payments.entity.Payment;
import com.emidru1.payments.service.PaymentService;
import com.emidru1.payments.utils.PaymentUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.MissingResourceException;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payments")
    public List<Long> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/payments/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .orElseThrow(() -> new MissingResourceException("Payment not found", Payment.class.getName(), id.toString()));
    }

    @PostMapping("/payments")
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody PaymentDto paymentDto) {
        System.out.println("Received payment DTO: " + paymentDto.toString());
        Payment payment = PaymentUtils.determinePaymentType(paymentDto);
        payment.validatePayment();
        return ResponseEntity.ok(paymentService.createPayment(payment));
    }

    @DeleteMapping("/payments/{id}")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }
}
