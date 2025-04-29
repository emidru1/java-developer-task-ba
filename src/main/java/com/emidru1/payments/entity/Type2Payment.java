package com.emidru1.payments.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
public class Type2Payment extends Payment {

    private static final double CANCELLATION_FEE_COEFFICIENT = 0.1;

    @Getter @Setter
    private String details;

    public Type2Payment() {
        super();
    }

    public Type2Payment(PaymentType type, BigDecimal amount, Currency currency, String debtorIban, String creditorIban, String details) {
        super(type, amount, currency, debtorIban, creditorIban, PaymentStatus.PENDING);
        this.details = details;
        validatePayment();
    }

    @Override
    public void validatePayment() {
        if (getCurrency() != getCurrency().USD || getCurrency() == null) {
            throw new IllegalArgumentException("Invalid currency type for Type1Payment");
        }
    }

    @Override
    public double getCancellationFeeCoefficient() {
        return CANCELLATION_FEE_COEFFICIENT;
    }
}
