package com.smartcare.hospital_management.service.payment;

import java.math.BigDecimal;

public class OnlinePayment implements PaymentProcessor {

    @Override
    public String processPayment(BigDecimal amount) {
        return "Online payment of Rs. " + amount + " processed";
    }
}