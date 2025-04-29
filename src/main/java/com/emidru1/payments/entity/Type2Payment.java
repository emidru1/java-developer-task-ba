package com.emidru1.payments.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
public class Type2Payment extends Payment {

    @Getter @Setter
    private String details;

    public Type2Payment() {
        super();
    }

    public Type2Payment(PaymentType type, BigDecimal amount, Currency currency, String debtorIban, String creditorIban, String details) {
        super(type, amount, currency, debtorIban, creditorIban);
        this.details = details;
        validatePayment();
    }

    @Override
    public void validatePayment() {
        if (getCurrency() != getCurrency().USD || getCurrency() == null) {
            throw new IllegalArgumentException("Invalid currency type for Type1Payment");
        }
    }
}
