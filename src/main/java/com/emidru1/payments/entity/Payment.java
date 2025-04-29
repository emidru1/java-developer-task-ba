package com.emidru1.payments.entity;

import com.emidru1.payments.entity.enums.Currency;
import com.emidru1.payments.entity.enums.PaymentStatus;
import com.emidru1.payments.entity.enums.PaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public abstract class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    private PaymentType type;
    @NotNull

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
    @NotNull
    private PaymentStatus status;

    @Getter @Setter
    private BigDecimal cancellationFee;

    @Getter @Setter
    @NotNull
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    public Payment() {
        // default constructor for JPA
    }

    public Payment(PaymentType type, BigDecimal amount, Currency currency, String debtorIban, String creditorIban, PaymentStatus status) {
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.debtorIban = debtorIban;
        this.creditorIban = creditorIban;
        this.status = status;
        this.cancellationFee = BigDecimal.ZERO;
    }

    public abstract void validatePayment();

    public abstract double getCancellationFeeCoefficient();
}
