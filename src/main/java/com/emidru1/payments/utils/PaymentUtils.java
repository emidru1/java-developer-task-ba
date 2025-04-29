package com.emidru1.payments.utils;

import com.emidru1.payments.dtos.PaymentDto;
import com.emidru1.payments.dtos.validators.Type1PaymentValidator;
import com.emidru1.payments.dtos.validators.Type2PaymentValidator;
import com.emidru1.payments.dtos.validators.Type3PaymentValidator;
import com.emidru1.payments.entity.Payment;
import com.emidru1.payments.entity.Type1Payment;
import com.emidru1.payments.entity.Type2Payment;
import com.emidru1.payments.entity.Type3Payment;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid payment type provided: " + paymentDto.getType());
        }
    }

    // Check if payment was made today
    public static boolean isPaymentCancellable(Payment payment) {
        LocalDate creationDate = payment.getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();
        return today.isEqual(creationDate);
    }

    // Calculate fee depending on how long ago the payment was created
    public static double calculateCancellationFee(Payment payment) {
        long hoursElapsed = Math.max(0, ChronoUnit.HOURS.between(payment.getCreatedAt(), LocalDateTime.now()));
        return payment.getCancellationFeeCoefficient() * hoursElapsed;
    }

    // Min and Max amounts should not be negative, nor should min be greater than max
    public static void validateAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        if (minAmount != null && maxAmount != null && minAmount.compareTo(maxAmount) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum amount cannot be greater than maximum amount");
        }
        if (minAmount != null && minAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum amount cannot be negative or equal to zero");
        }
        if (maxAmount != null && maxAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum amount cannot be negative or equal to zero");
        }
    }
}
