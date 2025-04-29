package com.emidru1.payments.dtos.validators;

import com.emidru1.payments.dtos.PaymentDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Type2PaymentValidator {

    public static void validate(PaymentDto paymentDto) {
        if (paymentDto.getBicCode() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BIC code cannot be used for TYPE3 payment");
        }
    }

}
