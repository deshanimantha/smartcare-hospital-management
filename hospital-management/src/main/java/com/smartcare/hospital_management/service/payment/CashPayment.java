package com.smartcare.hospital_management.service.payment;

import java.math.BigDecimal;

public class CashPayment implements PaymentProcessor {

    @Override
    public String processPayment(BigDecimal amount) {
        return "Cash payment of Rs. " + amount + " processed";
    }
}
