package com.emidru1.payments.dtos.validators;

import com.emidru1.payments.dtos.PaymentDto;

public class Type1PaymentValidator {

    public static void validate(PaymentDto paymentDto) {
        if (paymentDto.getDetails() == null) {
            throw new IllegalArgumentException("Details cannot be null for TYPE1 payment");
        }
        if (paymentDto.getBicCode() != null) {
            throw new IllegalArgumentException("BIC code cannot be used for TYPE3 payment");
        }
    }
}
