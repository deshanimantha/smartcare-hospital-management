package com.smartcare.hospital_management.service;

import com.smartcare.hospital_management.service.payment.PaymentProcessor;
import com.smartcare.hospital_management.service.payment.CashPayment;
import com.smartcare.hospital_management.service.payment.CardPayment;
import com.smartcare.hospital_management.service.payment.OnlinePayment;
import com.smartcare.hospital_management.entity.Bill;
import com.smartcare.hospital_management.entity.Payment;
import com.smartcare.hospital_management.exception.ResourceNotFoundException;
import com.smartcare.hospital_management.repository.BillRepository;
import com.smartcare.hospital_management.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            BillRepository billRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
    }

    @Transactional
    public Payment recordPayment(
            Payment payment,
            Long billId
    ) {
        Bill bill = getBillById(billId);

        if (!payment.validateAmount()) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero"
            );
        }

        if (bill.getTotalAmount() == null) {
            bill.calculateTotal();
        }

        BigDecimal alreadyPaid =
                paymentRepository
                        .findByBillBillId(billId)
                        .stream()
                        .map(Payment::getPaymentAmount)
                        .filter(Objects::nonNull)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal newPaidTotal =
                alreadyPaid.add(
                        payment.getPaymentAmount()
                );

        if (newPaidTotal.compareTo(
                bill.getTotalAmount()
        ) > 0) {
            throw new IllegalArgumentException(
                    "Payment exceeds remaining bill amount"
            );
        }

        payment.setBill(bill);
        payment.recordPayment();
        PaymentProcessor processor =
                selectPaymentProcessor(payment.getPaymentMethod());

        String paymentMessage =
                processor.processPayment(payment.getPaymentAmount());

        System.out.println(paymentMessage);

        Payment saved =
                paymentRepository.save(payment);

        if (newPaidTotal.compareTo(
                bill.getTotalAmount()
        ) == 0) {
            bill.updatePaymentStatus("PAID");
        } else {
            bill.updatePaymentStatus("PARTIALLY_PAID");
        }

        billRepository.save(bill);

        return saved;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with ID: "
                                        + paymentId
                        )
                );
    }

    public List<Payment> getPaymentsByBill(
            Long billId
    ) {
        getBillById(billId);

        return paymentRepository
                .findByBillBillId(billId);
    }

    private Bill getBillById(Long billId) {
        return billRepository.findById(billId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill not found with ID: "
                                        + billId
                        )
                );
    }
    private PaymentProcessor selectPaymentProcessor(String paymentMethod) {

        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("Payment method is required");
        }

        return switch (paymentMethod.toUpperCase()) {
            case "CASH" -> new CashPayment();
            case "CARD" -> new CardPayment();
            case "ONLINE" -> new OnlinePayment();
            default -> throw new IllegalArgumentException(
                    "Invalid payment method: " + paymentMethod
            );
        };
    }
}