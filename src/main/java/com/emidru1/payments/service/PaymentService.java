package com.emidru1.payments.service;

import com.emidru1.payments.entity.Payment;
import com.emidru1.payments.entity.PaymentStatus;
import com.emidru1.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Long> getFilteredPayments(BigDecimal minAmount, BigDecimal maxAmount) {
        return paymentRepository.findByStatusAndAmountBetween(PaymentStatus.PENDING, minAmount, maxAmount)
                .stream()
                .map(Payment::getId)
                .toList();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
}
