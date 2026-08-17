package com.smartcare.hospital_management.service.payment;

import java.math.BigDecimal;

public class CardPayment implements PaymentProcessor {

    @Override
    public String processPayment(BigDecimal amount) {
        return "Card payment of Rs. " + amount + " processed";
    }
}
