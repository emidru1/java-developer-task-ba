package com.emidru1.payments.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
public class Type3Payment extends Payment {

    @Getter @Setter
    private String bicCode;

    public Type3Payment() {
        super();
    }

    public Type3Payment(PaymentType type, BigDecimal amount, Currency currency, String debtor_iban, String creditor_iban, String bicCode) {
        super(type, amount, currency, debtor_iban, creditor_iban);
        this.bicCode = bicCode;
        validatePayment();
    }

    @Override
    public void validatePayment() {
        if (getCurrency() == null) {
            throw new IllegalArgumentException("Invalid currency type");
        }
        if (bicCode == null || bicCode.isEmpty()) {
            throw new IllegalArgumentException("BIC code cannot be null or empty");
        }
    }
}
