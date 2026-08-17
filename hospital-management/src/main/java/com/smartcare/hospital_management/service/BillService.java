package com.smartcare.hospital_management.service;

import com.smartcare.hospital_management.entity.Admission;
import com.smartcare.hospital_management.entity.Appointment;
import com.smartcare.hospital_management.entity.Bill;
import com.smartcare.hospital_management.entity.Patient;
import com.smartcare.hospital_management.exception.ResourceNotFoundException;
import com.smartcare.hospital_management.repository.AdmissionRepository;
import com.smartcare.hospital_management.repository.AppointmentRepository;
import com.smartcare.hospital_management.repository.BillRepository;
import com.smartcare.hospital_management.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final AdmissionRepository admissionRepository;

    public BillService(
            BillRepository billRepository,
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            AdmissionRepository admissionRepository
    ) {
        this.billRepository = billRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.admissionRepository = admissionRepository;
    }

    public Bill generateAppointmentBill(
            Bill bill,
            Long patientId,
            Long appointmentId
    ) {
        Patient patient = getPatientById(patientId);

        Appointment appointment =
                appointmentRepository
                        .findById(appointmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found with ID: "
                                                + appointmentId
                                )
                        );

        if (!Objects.equals(
                appointment.getPatient().getPatientId(),
                patientId
        )) {
            throw new IllegalArgumentException(
                    "Appointment does not belong to this patient"
            );
        }

        bill.setPatient(patient);
        bill.setAppointment(appointment);

        prepareBill(bill);

        return billRepository.save(bill);
    }

    public Bill generateAdmissionBill(
            Bill bill,
            Long patientId,
            Long admissionId
    ) {
        Patient patient = getPatientById(patientId);

        Admission admission = admissionRepository
                .findById(admissionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Admission not found with ID: "
                                        + admissionId
                        )
                );

        if (!Objects.equals(
                admission.getPatient().getPatientId(),
                patientId
        )) {
            throw new IllegalArgumentException(
                    "Admission does not belong to this patient"
            );
        }

        bill.setPatient(patient);
        bill.setAdmission(admission);

        prepareBill(bill);

        return billRepository.save(bill);
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill getBillById(Long billId) {
        return billRepository.findById(billId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill not found with ID: "
                                        + billId
                        )
                );
    }

    public List<Bill> getBillsByPatient(
            Long patientId
    ) {
        return billRepository
                .findByPatientPatientId(patientId);
    }

    public List<Bill> getBillsByStatus(
            String status
    ) {
        return billRepository
                .findByPaymentStatusIgnoreCase(status);
    }

    public Bill recalculateBill(Long billId) {
        Bill bill = getBillById(billId);
        bill.calculateTotal();

        return billRepository.save(bill);
    }

    public void deleteBill(Long billId) {
        billRepository.delete(getBillById(billId));
    }

    private Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with ID: "
                                        + patientId
                        )
                );
    }

    private void prepareBill(Bill bill) {
        validateCharge(
                bill.getConsultationCharge(),
                "Consultation charge"
        );
        validateCharge(
                bill.getRoomCharge(),
                "Room charge"
        );
        validateCharge(
                bill.getLaboratoryCharge(),
                "Laboratory charge"
        );
        validateCharge(
                bill.getMedicineCharge(),
                "Medicine charge"
        );

        bill.setBillDate(LocalDate.now());
        bill.setPaymentStatus("UNPAID");
        bill.calculateTotal();

        if (bill.getTotalAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Bill total must be greater than zero"
            );
        }
    }

    private void validateCharge(
            BigDecimal charge,
            String fieldName
    ) {
        if (charge != null
                && charge.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be negative"
            );
        }
    }
}
