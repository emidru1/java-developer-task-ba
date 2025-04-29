package com.emidru1.payments.utils;

import com.emidru1.payments.dtos.PaymentDto;
import com.emidru1.payments.dtos.validators.Type1PaymentValidator;
import com.emidru1.payments.dtos.validators.Type2PaymentValidator;
import com.emidru1.payments.dtos.validators.Type3PaymentValidator;
import com.emidru1.payments.entity.Payment;
import com.emidru1.payments.entity.Type1Payment;
import com.emidru1.payments.entity.Type2Payment;
import com.emidru1.payments.entity.Type3Payment;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PaymentUtils {

    private PaymentUtils() {
    }

    public static Payment determinePaymentType(PaymentDto paymentDto) {
        switch (paymentDto.getType()) {
            case TYPE1:
                Type1PaymentValidator.validate(paymentDto);
                return new Type1Payment(paymentDto.getType(), paymentDto.getAmount(), paymentDto.getCurrency(), paymentDto.getCreditorIban(), paymentDto.getDebtorIban(), paymentDto.getDetails());
            case TYPE2:
                Type2PaymentValidator.validate(paymentDto);
                return new Type2Payment(paymentDto.getType(), paymentDto.getAmount(), paymentDto.getCurrency(), paymentDto.getCreditorIban(), paymentDto.getDebtorIban(), paymentDto.getDetails());
            case TYPE3:
                Type3PaymentValidator.validate(paymentDto);
                return new Type3Payment(paymentDto.getType(), paymentDto.getAmount(), paymentDto.getCurrency(), paymentDto.getCreditorIban(), paymentDto.getDebtorIban(), paymentDto.getBicCode());
            default:
                throw new IllegalArgumentException("Invalid payment type: " + paymentDto.getType());
        }
    }

    public static boolean isPaymentCancellable(Payment payment) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = payment.getCreatedAt().toLocalDate().plusDays(1).atStartOfDay();
        return now.isBefore(createdAt);
    }

    public static double calculateCancellationFee(Payment payment) {
        return Math.floor(ChronoUnit.HOURS.between(payment.getCreatedAt(), LocalDateTime.now()));
    }
}
