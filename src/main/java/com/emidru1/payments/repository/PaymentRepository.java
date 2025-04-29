package com.emidru1.payments.repository;

import com.emidru1.payments.entity.Payment;
import com.emidru1.payments.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStatusAndAmountBetween(PaymentStatus status, BigDecimal minAmount, BigDecimal maxAmount);
}
