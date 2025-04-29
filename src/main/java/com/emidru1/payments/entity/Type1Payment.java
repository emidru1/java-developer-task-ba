package com.emidru1.payments.entity;

import com.emidru1.payments.entity.enums.Currency;
import com.emidru1.payments.entity.enums.PaymentStatus;
import com.emidru1.payments.entity.enums.PaymentType;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
public class Type1Payment extends Payment {

    private final static double CANCELLATION_FEE_COEFFICIENT = 0.05;

    @Getter @Setter
    private String details;

    public Type1Payment() {
        super();
    }

    public Type1Payment(PaymentType type, BigDecimal amount, Currency currency, String debtorIban, String creditorIban, String details) {
        super(type, amount, currency, debtorIban, creditorIban, PaymentStatus.PENDING);
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

    @Override
    public double getCancellationFeeCoefficient() {
        return CANCELLATION_FEE_COEFFICIENT;
    }
}
