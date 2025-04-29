package com.emidru1.payments.service;

import com.emidru1.payments.dtos.PaymentLookupDto;
import com.emidru1.payments.entity.Payment;
import com.emidru1.payments.entity.enums.PaymentStatus;
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
        List<Payment> payments;

        if (minAmount == null && maxAmount == null) {
            payments = paymentRepository.findByStatus(PaymentStatus.PENDING);
        } else if (minAmount == null) {
            payments = paymentRepository.findByStatusAndAmountLessThan(PaymentStatus.PENDING, maxAmount);
        } else if (maxAmount == null) {
            payments = paymentRepository.findByStatusAndAmountGreaterThan(PaymentStatus.PENDING, minAmount);
        } else {
            payments = paymentRepository.findByStatusAndAmountBetween(PaymentStatus.PENDING, minAmount, maxAmount);
        }

        return payments.stream().map(Payment::getId).toList();
    }


    public Optional<PaymentLookupDto> getPaymentLookupDtoById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            PaymentLookupDto paymentLookupDto = new PaymentLookupDto(payment.get().getId(), payment.get().getCancellationFee());
            return Optional.of(paymentLookupDto);
        }
        return Optional.empty();
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
