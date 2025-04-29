package com.emidru1.payments.dtos.validators;

import com.emidru1.payments.dtos.PaymentDto;

public class Type2PaymentValidator {

    public static void validate(PaymentDto paymentDto) {
        if (paymentDto.getBicCode() != null) {
            throw new IllegalArgumentException("BIC code cannot be used for TYPE3 payment");
        }
    }

}
