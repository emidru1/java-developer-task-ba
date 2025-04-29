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
    private String debtorIban;

    @Getter @Setter
    private String creditorIban;

    public Payment() {
        // default constructor for JPA
    }

    public Payment(PaymentType type, BigDecimal amount, Currency currency, String debtorIban, String creditorIban) {
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.debtorIban = debtorIban;
        this.creditorIban = creditorIban;
    }

    public abstract void validatePayment();
}
