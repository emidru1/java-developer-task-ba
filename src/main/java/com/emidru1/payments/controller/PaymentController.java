package com.emidru1.payments.controller;

import com.emidru1.payments.dtos.PaymentDto;
import com.emidru1.payments.dtos.PaymentLookupDto;
import com.emidru1.payments.entity.Payment;
import com.emidru1.payments.entity.enums.PaymentStatus;
import com.emidru1.payments.service.PaymentService;
import com.emidru1.payments.utils.LoggingUtils;
import com.emidru1.payments.utils.PaymentUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payments")
    public List<Long> getPayments(HttpServletRequest request, @RequestParam (required = false) BigDecimal minAmount,
                                  @RequestParam (required = false) BigDecimal maxAmount) {
        PaymentUtils.validateAmountRange(minAmount, maxAmount);
        LoggingUtils.logIpAndCountry(request);
        return paymentService.getFilteredPayments(minAmount, maxAmount);
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<PaymentLookupDto> getPaymentLookupDtoById(HttpServletRequest request, @PathVariable Long id) {
        LoggingUtils.logIpAndCountry(request);
        PaymentLookupDto lookupDto = paymentService.getPaymentLookupDtoById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        return ResponseEntity.ok(lookupDto);
    }

    @PostMapping("/payments")
    public ResponseEntity<Payment> createPayment(HttpServletRequest request, @Valid @RequestBody PaymentDto paymentDto) {
        LoggingUtils.logIpAndCountry(request);
        Payment payment = PaymentUtils.determinePaymentType(paymentDto);
        return ResponseEntity.ok(paymentService.createPayment(payment));
    }

    @PutMapping("/payments/{id}")
    public ResponseEntity<Payment> cancelPayment(HttpServletRequest request, @PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        if (!PaymentUtils.isPaymentCancellable(payment)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment cannot be cancelled the next day after creation");
        }
        LoggingUtils.logIpAndCountry(request);
        double cancellationFee = PaymentUtils.calculateCancellationFee(payment);

        payment.setCancellationFee(BigDecimal.valueOf(cancellationFee));
        payment.setStatus(PaymentStatus.CANCELLED);

        return ResponseEntity.ok(paymentService.updatePayment(payment));
    }
}
