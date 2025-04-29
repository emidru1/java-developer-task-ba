package com.emidru1.payments.controller;

import com.emidru1.payments.dtos.PaymentDto;
import com.emidru1.payments.entity.Payment;
import com.emidru1.payments.entity.PaymentStatus;
import com.emidru1.payments.service.PaymentService;
import com.emidru1.payments.utils.PaymentUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
                .orElseThrow(() -> new MissingResourceException("Payment not found", Payment.class.getName(), id.toString())); // need to find more suitable exception here
    }

    @PostMapping("/payments")
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody PaymentDto paymentDto) {
        System.out.println("Received payment DTO: " + paymentDto.toString());
        Payment payment = PaymentUtils.determinePaymentType(paymentDto);
        return ResponseEntity.ok(paymentService.createPayment(payment));
    }

    @PutMapping("/payments/{id}")
    public void cancelPayment(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id)
                .orElseThrow(() -> new MissingResourceException("Payment not found", Payment.class.getName(), id.toString()));

        if (!PaymentUtils.isPaymentCancellable(payment)) {
            throw new IllegalArgumentException("Payment cannot be cancelled next day after creation");
        }

        double cancellationFee = PaymentUtils.calculateCancellationFee(payment);
        payment.setCancellationFee(BigDecimal.valueOf(cancellationFee));
        payment.setStatus(PaymentStatus.CANCELLED);

        paymentService.updatePayment(payment);
    }
}
