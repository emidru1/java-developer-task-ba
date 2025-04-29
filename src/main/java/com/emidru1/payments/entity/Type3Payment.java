package com.emidru1.payments.entity;

import com.emidru1.payments.entity.enums.Currency;
import com.emidru1.payments.entity.enums.PaymentStatus;
import com.emidru1.payments.entity.enums.PaymentType;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
public class Type3Payment extends Payment {

    private static final double CANCELLATION_FEE_COEFFICIENT = 0.15;

    @Getter @Setter
    private String bicCode;

    public Type3Payment() {
        super();
    }

    public Type3Payment(PaymentType type, BigDecimal amount, Currency currency, String debtorIban, String creditorIban, String bicCode) {
        super(type, amount, currency, debtorIban, creditorIban, PaymentStatus.PENDING);
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

    @Override
    public double getCancellationFeeCoefficient() {
        return CANCELLATION_FEE_COEFFICIENT;
    }
}
