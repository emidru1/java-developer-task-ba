package com.emidru1.payments.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class PaymentLookupDto {
    @Getter @Setter
    private Long id;

    @Getter @Setter
    private BigDecimal cancellationFee;

    public PaymentLookupDto() {
        // default constructor for JPA
    }

    public PaymentLookupDto(Long id, BigDecimal cancellationFee) {
        this.id = id;
        this.cancellationFee = cancellationFee;
    }
}
