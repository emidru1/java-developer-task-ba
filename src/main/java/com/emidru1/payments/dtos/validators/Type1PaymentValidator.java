package com.emidru1.payments.dtos.validators;

import com.emidru1.payments.dtos.PaymentDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Type1PaymentValidator {

    public static void validate(PaymentDto paymentDto) {
        if (paymentDto.getDetails() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Details cannot be null for TYPE1 payment");
        }
        if (paymentDto.getBicCode() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BIC code cannot be used for TYPE3 payment");
        }
    }
}
