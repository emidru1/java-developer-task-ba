package com.emidru1.payments.dtos;

import com.emidru1.payments.entity.Currency;
import com.emidru1.payments.entity.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class PaymentDto {

    @Getter @Setter
    @NotNull
    private PaymentType type;

    @Getter @Setter
    @NotNull
    private BigDecimal amount;

    @Getter @Setter
    @NotNull
    private Currency currency;

    @Getter @Setter
    @NotNull
    private String debtor_iban;

    @Getter @Setter
    @NotNull
    private String creditor_iban;

    @Getter @Setter
    private String details;

    @Getter @Setter
    private String bicCode;

    public PaymentDto() {
        // default constructor for JPA
    }

    public PaymentDto(PaymentType type, BigDecimal amount, Currency currency, String debtor_iban, String creditor_iban, String details, String bicCode) {
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.debtor_iban = debtor_iban;
        this.creditor_iban = creditor_iban;
        this.details = details;
        this.bicCode = bicCode;
    }

    @Override
    public String toString() {
        return "PaymentDto{" +
                "type=" + type +
                ", amount=" + amount +
                ", currency=" + currency +
                ", debtor_iban='" + debtor_iban + '\'' +
                ", creditor_iban='" + creditor_iban + '\'' +
                ", details='" + details + '\'' +
                ", bicCode='" + bicCode + '\'' +
                '}';
    }
}
