package com.emidru1.payments.dtos;

import com.emidru1.payments.entity.enums.Currency;
import com.emidru1.payments.entity.enums.PaymentStatus;
import com.emidru1.payments.entity.enums.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentDto {

    @Getter @Setter
    @NotNull
    private PaymentType type;

    @Getter @Setter
    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Getter @Setter
    @NotNull
    private Currency currency;

    @Getter @Setter
    @NotBlank
    private String debtorIban;

    @Getter @Setter
    @NotBlank
    private String creditorIban;

    @Getter @Setter
    private PaymentStatus status;

    @Getter @Setter
    private Date createdAt;

    @Getter @Setter
    private String details;

    @Getter @Setter
    @NotBlank
    private String bicCode;

    public PaymentDto() {
        // default constructor for JPA
    }

    public PaymentDto(PaymentType type, BigDecimal amount, Currency currency, String debtorIban, String creditorIban, PaymentStatus status, String details, String bicCode) {
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.debtorIban = debtorIban;
        this.creditorIban = creditorIban;
        this.status = status;
        this.details = details;
        this.bicCode = bicCode;
    }

    // Remove after done debugging
    @Override
    public String toString() {
        return "PaymentDto{" +
                "type=" + type +
                ", amount=" + amount +
                ", currency=" + currency +
                ", debtorIban='" + debtorIban + '\'' +
                ", creditorIban='" + creditorIban + '\'' +
                ", status=" + status + '\'' +
                ", details='" + details + '\'' +
                ", bicCode='" + bicCode + '\'' +
                '}';
    }
}
