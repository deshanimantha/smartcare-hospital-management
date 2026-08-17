package com.smartcare.hospital_management.service.payment;

import java.math.BigDecimal;

public interface PaymentProcessor {

    String processPayment(BigDecimal amount);
}
