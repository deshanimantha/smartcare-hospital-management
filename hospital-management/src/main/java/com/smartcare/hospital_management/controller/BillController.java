package com.smartcare.hospital_management.controller;

import com.smartcare.hospital_management.entity.Bill;
import com.smartcare.hospital_management.service.BillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping(
            "/patient/{patientId}/appointment/{appointmentId}"
    )
    public ResponseEntity<Bill> createAppointmentBill(
            @PathVariable Long patientId,
            @PathVariable Long appointmentId,
            @RequestBody Bill bill
    ) {
        Bill saved = billService
                .generateAppointmentBill(
                        bill,
                        patientId,
                        appointmentId
                );

        return new ResponseEntity<>(
                saved,
                HttpStatus.CREATED
        );
    }

    @PostMapping(
            "/patient/{patientId}/admission/{admissionId}"
    )
    public ResponseEntity<Bill> createAdmissionBill(
            @PathVariable Long patientId,
            @PathVariable Long admissionId,
            @RequestBody Bill bill
    ) {
        Bill saved = billService.generateAdmissionBill(
                bill,
                patientId,
                admissionId
        );

        return new ResponseEntity<>(
                saved,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }

    @GetMapping("/{billId}")
    public Bill getBillById(
            @PathVariable Long billId
    ) {
        return billService.getBillById(billId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Bill> getBillsByPatient(
            @PathVariable Long patientId
    ) {
        return billService
                .getBillsByPatient(patientId);
    }

    @GetMapping("/status")
    public List<Bill> getBillsByStatus(
            @RequestParam String value
    ) {
        return billService.getBillsByStatus(value);
    }

    @PutMapping("/{billId}/calculate")
    public Bill recalculateBill(
            @PathVariable Long billId
    ) {
        return billService.recalculateBill(billId);
    }

    @DeleteMapping("/{billId}")
    public ResponseEntity<Void> deleteBill(
            @PathVariable Long billId
    ) {
        billService.deleteBill(billId);
        return ResponseEntity.noContent().build();
    }
}
