package com.emidru1.payments.utils;

import com.emidru1.payments.dtos.PaymentDto;
import com.emidru1.payments.entity.Payment;
import com.emidru1.payments.entity.Type1Payment;
import com.emidru1.payments.entity.Type2Payment;
import com.emidru1.payments.entity.Type3Payment;
import org.springframework.web.bind.annotation.RequestBody;

public class PaymentUtils {

    private PaymentUtils() {
    }

    public static Payment determinePaymentType(@RequestBody PaymentDto paymentDto) {
        switch (paymentDto.getType()) {
            case TYPE1:
                if (paymentDto.getDetails() == null) {
                    throw new IllegalArgumentException("Details cannot be null for TYPE1 payment");
                }
                if (paymentDto.getBicCode() != null) {
                    throw new IllegalArgumentException("BIC code must be null for TYPE1 payment");
                }
                return new Type1Payment(paymentDto.getType(), paymentDto.getAmount(), paymentDto.getCurrency(), paymentDto.getCreditor_iban(), paymentDto.getDebtor_iban(), paymentDto.getDetails());
            case TYPE2:
                if (paymentDto.getBicCode() != null) {
                    throw new IllegalArgumentException("BIC code must be null for TYPE1 payment");
                }
                return new Type2Payment(paymentDto.getType(), paymentDto.getAmount(), paymentDto.getCurrency(), paymentDto.getCreditor_iban(), paymentDto.getDebtor_iban(), paymentDto.getDetails());
            case TYPE3:
                if (paymentDto.getDetails() != null) {
                    throw new IllegalArgumentException("Details must be null for TYPE3 payment");
                }
                if (paymentDto.getBicCode() == null) {
                    throw new IllegalArgumentException("BIC code cannot be null for TYPE3 payment");
                }
                return new Type3Payment(paymentDto.getType(), paymentDto.getAmount(), paymentDto.getCurrency(), paymentDto.getCreditor_iban(), paymentDto.getDebtor_iban(), paymentDto.getBicCode());
            default:
                throw new IllegalArgumentException("Invalid payment type: " + paymentDto.getType());
        }
    }
}
