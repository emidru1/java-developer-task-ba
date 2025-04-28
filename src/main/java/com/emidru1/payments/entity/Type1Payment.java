package com.emidru1.payments.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
public class Type1Payment extends Payment {

    @Getter @Setter
    private String details;

    public Type1Payment() {
        super();
    }

    public Type1Payment(PaymentType type, BigDecimal amount, Currency currency, String debtor_iban, String creditor_iban, String details) {
        super(type, amount, currency, debtor_iban, creditor_iban);
        this.details = details;
        validatePayment();
    }

    @Override
    public void validatePayment() {
        if (getCurrency() != getCurrency().EUR || getCurrency() == null) {
            throw new IllegalArgumentException("Invalid currency type for Type1Payment");
        }
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("Details cannot be null or empty");
        }
    }
}
