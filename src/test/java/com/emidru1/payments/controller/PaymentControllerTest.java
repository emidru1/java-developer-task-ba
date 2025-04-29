package com.emidru1.payments.controller;

import com.emidru1.payments.entity.Payment;
import com.emidru1.payments.entity.Type1Payment;
import com.emidru1.payments.entity.Type2Payment;
import com.emidru1.payments.entity.enums.Currency;
import com.emidru1.payments.entity.enums.PaymentStatus;
import com.emidru1.payments.entity.enums.PaymentType;
import com.emidru1.payments.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setup() {
        paymentRepository.deleteAll();

        Type1Payment payment = new Type1Payment(
                PaymentType.TYPE1,
                BigDecimal.valueOf(100),
                Currency.EUR,
                "LT1293812371230",
                "LT1293812371238",
                "Test details"
        );
        paymentRepository.save(payment);

        Type2Payment payment2 = new Type2Payment(
                PaymentType.TYPE2,
                BigDecimal.valueOf(25),
                Currency.USD,
                "LT1293812371230",
                "LT1293812371238",
                "Test details"
        );
        paymentRepository.save(payment2);

        //double save to override the auto generated createdAt by repository
        payment2.setCreatedAt(LocalDateTime.now().minusDays(2));
        paymentRepository.save(payment2);
    }


    @Test
    void testShouldReturnUnfilteredListWhenNoFiltersAreApplied() throws Exception {
        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testShouldReturnEmptyListWhenNoPaymentsMatchFilters() throws Exception {
        mockMvc.perform(get("/api/payments")
                        .param("minAmount", "200")
                        .param("maxAmount", "300"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testShouldReturnFilteredListWhenFiltersAreApplied() throws Exception {
        mockMvc.perform(get("/api/payments")
                        .param("minAmount", "50")
                        .param("maxAmount", "150"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testShouldReturnPaymentById() throws Exception {
        Payment payment = paymentRepository.findAll().get(0);
        mockMvc.perform(get("/api/payments/" + payment.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(payment.getId()));
    }

    @Test
    void testShouldReturnNotFoundWhenPaymentDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/payments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testShouldCreateType3PaymentSuccessfully() throws Exception {
        String paymentJson = """
        {
            "type": "TYPE3",
            "amount": 123.12,
            "currency": "EUR",
            "debtorIban": "LT1293812371238",
            "creditorIban": "LT1293812371230",
            "bicCode": "testBicCode"
        }
        """;

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount").value(123.12))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testShouldReturnBadRequestWhenPaymentTypeIsInvalid() throws Exception {
        String paymentJson = """
        {
            "type": "TYPE4",
            "amount": 123.12,
            "currency": "EUR",
            "debtorIban": "LT1293812371238",
            "creditorIban": "LT1293812371230",
            "bicCode": "testBicCode"
        }
        """;

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testShouldReturnBadRequestWhenPaymentAmountIsNegative() throws Exception {
        String paymentJson = """
        {
            "type": "TYPE1",
            "amount": -123.12,
            "currency": "EUR",
            "debtorIban": "LT1293812371238",
            "creditorIban": "LT1293812371230",
            "bicCode": "testBicCode"
        }
        """;

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testShouldCancelPaymentSuccessfully() throws Exception {
        Payment payment = paymentRepository.findAll().get(0);

        mockMvc.perform(put("/api/payments/" + payment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.cancellationFee").value("0.0"));
    }

    @Test
    void testShouldNotAllowToCancelYesterdaysPayment() throws Exception {
        Payment payment = paymentRepository.findAll().get(1);

        mockMvc.perform(put("/api/payments/" + payment.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testShouldReturnNotFoundWhenCancellingNonExistentPayment() throws Exception {
        mockMvc.perform(put("/api/payments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testShouldReturnBadRequestWhenMinAmountIsGreaterThanMaxAmount() throws Exception {
        mockMvc.perform(get("/api/payments")
                        .param("minAmount", "200")
                        .param("maxAmount", "100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testShouldReturnBadRequestWhenMinAmountIsNegative() throws Exception {
        mockMvc.perform(get("/api/payments")
                        .param("minAmount", "-100")
                        .param("maxAmount", "200"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testShouldReturnBadRequestWhenMaxAmountIsNegative() throws Exception {
        mockMvc.perform(get("/api/payments")
                        .param("minAmount", "100")
                        .param("maxAmount", "-200"))
                .andExpect(status().isBadRequest());
    }

}
