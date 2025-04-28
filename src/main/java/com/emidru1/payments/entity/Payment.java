package com.emidru1.payments.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    private PaymentType type;

    @Getter @Setter
    private BigDecimal amount;

    @Getter @Setter
    private Currency currency;

    @Getter @Setter
    private String debtor_iban;

    @Getter @Setter
    private String creditor_iban;

    public Payment() {
        // default constructor for JPA
    }

    public Payment(PaymentType type, BigDecimal amount, Currency currency, String debtor_iban, String creditor_iban) {
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.debtor_iban = debtor_iban;
        this.creditor_iban = creditor_iban;
    }

    public abstract void validatePayment();
}
