DROP DATABASE IF EXISTS SmartCare_Hospital;

CREATE DATABASE SmartCare_Hospital;

USE SmartCare_Hospital;

CREATE TABLE Department (
    department_id BIGINT AUTO_INCREMENT,
    department_name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    head_doctor_id BIGINT,

    PRIMARY KEY (department_id),

    UNIQUE (department_name)
);


CREATE TABLE Doctor (
    doctor_id BIGINT AUTO_INCREMENT,
    department_id BIGINT NOT NULL,
    doctor_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    qualification VARCHAR(150) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    consultation_fee DECIMAL(10,2) NOT NULL,

    PRIMARY KEY (doctor_id),

    UNIQUE (contact_number),

    CONSTRAINT fk_doctor_department
        FOREIGN KEY (department_id)
        REFERENCES Department(department_id),

    CONSTRAINT chk_doctor_fee
        CHECK (consultation_fee >= 0)
);

ALTER TABLE Department
ADD CONSTRAINT fk_department_head
FOREIGN KEY (head_doctor_id)
REFERENCES Doctor(doctor_id);


CREATE TABLE Staff (
    staff_id BIGINT AUTO_INCREMENT,
    department_id BIGINT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    staff_role VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,

    PRIMARY KEY (staff_id),

    UNIQUE (contact_number),

    CONSTRAINT fk_staff_department
        FOREIGN KEY (department_id)
        REFERENCES Department(department_id)
);


CREATE TABLE Doctor_Schedule (
    schedule_id BIGINT AUTO_INCREMENT,
    doctor_id BIGINT NOT NULL,
    day_of_week VARCHAR(15) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    availability_status VARCHAR(20) NOT NULL DEFAULT 'Available',

    PRIMARY KEY (schedule_id),

    CONSTRAINT fk_schedule_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES Doctor(doctor_id),

    CONSTRAINT chk_schedule_day
        CHECK (
            day_of_week IN (
                'Monday',
                'Tuesday',
                'Wednesday',
                'Thursday',
                'Friday',
                'Saturday',
                'Sunday'
            )
        ),

    CONSTRAINT chk_schedule_time
        CHECK (end_time > start_time),

    CONSTRAINT chk_schedule_status
        CHECK (
            availability_status IN (
                'Available',
                'Unavailable'
            )
        )
);


CREATE TABLE Patient (
    patient_id BIGINT AUTO_INCREMENT,
    full_name VARCHAR(150) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(10) NOT NULL,
    address VARCHAR(255) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    blood_group VARCHAR(5),
    emergency_contact VARCHAR(20) NOT NULL,

    PRIMARY KEY (patient_id),

    UNIQUE (contact_number),

    CONSTRAINT chk_patient_gender
        CHECK (
            gender IN (
                'Male',
                'Female',
                'Other'
            )
        ),

    CONSTRAINT chk_patient_blood_group
        CHECK (
            blood_group IN (
                'A+',
                'A-',
                'B+',
                'B-',
                'AB+',
                'AB-',
                'O+',
                'O-'
            )
        )
);


CREATE TABLE Appointment (
    appointment_id BIGINT AUTO_INCREMENT,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    appointment_status VARCHAR(20) NOT NULL DEFAULT 'Scheduled',
    consultation_room VARCHAR(50) NOT NULL,
    reason VARCHAR(255),

    PRIMARY KEY (appointment_id),

    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id)
        REFERENCES Patient(patient_id),

    CONSTRAINT fk_appointment_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES Doctor(doctor_id),

    CONSTRAINT chk_appointment_status
        CHECK (
            appointment_status IN (
                'Scheduled',
                'Completed',
                'Cancelled',
                'No Show'
            )
        ),

    UNIQUE (
        doctor_id,
        appointment_date,
        appointment_time
    )
);

CREATE TABLE Treatment (
    treatment_id BIGINT AUTO_INCREMENT,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    diagnosis VARCHAR(255) NOT NULL,
    prescription TEXT,
    treatment_notes TEXT,
    treatment_date DATE NOT NULL,
    medicine_charge DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    PRIMARY KEY (treatment_id),

    CONSTRAINT fk_treatment_patient
        FOREIGN KEY (patient_id)
        REFERENCES Patient(patient_id),

    CONSTRAINT fk_treatment_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES Doctor(doctor_id),

    CONSTRAINT chk_medicine_charge
        CHECK (medicine_charge >= 0)
);


CREATE TABLE Laboratory_Test (
    lab_test_id BIGINT AUTO_INCREMENT,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    technician_id BIGINT,
    test_name VARCHAR(100) NOT NULL,
    test_date DATE NOT NULL,
    test_result TEXT,
    test_status VARCHAR(20) NOT NULL DEFAULT 'Pending',
    laboratory_charge DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    PRIMARY KEY (lab_test_id),

    CONSTRAINT fk_lab_patient
        FOREIGN KEY (patient_id)
        REFERENCES Patient(patient_id),

    CONSTRAINT fk_lab_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES Doctor(doctor_id),

    CONSTRAINT fk_lab_technician
        FOREIGN KEY (technician_id)
        REFERENCES Staff(staff_id),

    CONSTRAINT chk_lab_status
        CHECK (
            test_status IN (
                'Pending',
                'Completed',
                'Cancelled'
            )
        ),

    CONSTRAINT chk_lab_charge
        CHECK (laboratory_charge >= 0)
);


CREATE TABLE Room (
    room_id BIGINT AUTO_INCREMENT,
    room_number VARCHAR(20) NOT NULL,
    room_category VARCHAR(30) NOT NULL,
    daily_charge DECIMAL(10,2) NOT NULL,
    room_status VARCHAR(20) NOT NULL DEFAULT 'Available',

    PRIMARY KEY (room_id),

    UNIQUE (room_number),

    CONSTRAINT chk_room_category
        CHECK (
            room_category IN (
                'General Ward',
                'Private Room',
                'ICU'
            )
        ),

    CONSTRAINT chk_room_charge
        CHECK (daily_charge >= 0),

    CONSTRAINT chk_room_status
        CHECK (
            room_status IN (
                'Available',
                'Occupied',
                'Maintenance'
            )
        )
);

CREATE TABLE Bed (
    bed_id BIGINT AUTO_INCREMENT,
    room_id BIGINT NOT NULL,
    bed_number VARCHAR(20) NOT NULL,
    availability_status VARCHAR(20) NOT NULL DEFAULT 'Available',

    PRIMARY KEY (bed_id),

    CONSTRAINT fk_bed_room
        FOREIGN KEY (room_id)
        REFERENCES Room(room_id),

    CONSTRAINT chk_bed_status
        CHECK (
            availability_status IN (
                'Available',
                'Occupied',
                'Maintenance'
            )
        ),

    UNIQUE (
        room_id,
        bed_number
    )
);


CREATE TABLE Admission (
    admission_id BIGINT AUTO_INCREMENT,
    patient_id BIGINT NOT NULL,
    bed_id BIGINT NOT NULL,
    admission_date DATE NOT NULL,
    discharge_date DATE,
    admission_status VARCHAR(20) NOT NULL DEFAULT 'Admitted',
    admission_notes TEXT,

    PRIMARY KEY (admission_id),

    CONSTRAINT fk_admission_patient
        FOREIGN KEY (patient_id)
        REFERENCES Patient(patient_id),

    CONSTRAINT fk_admission_bed
        FOREIGN KEY (bed_id)
        REFERENCES Bed(bed_id),

    CONSTRAINT chk_admission_status
        CHECK (
            admission_status IN (
                'Admitted',
                'Discharged',
                'Cancelled'
            )
        ),

    CONSTRAINT chk_discharge_date
        CHECK (
            discharge_date IS NULL
            OR discharge_date >= admission_date
        )
);


CREATE TABLE Bill (
    bill_id BIGINT AUTO_INCREMENT,
    patient_id BIGINT NOT NULL,
    appointment_id BIGINT,
    admission_id BIGINT,
    bill_date DATE NOT NULL,

    consultation_charge DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    room_charge DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    laboratory_charge DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    medicine_charge DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    total_amount DECIMAL(10,2)
        GENERATED ALWAYS AS (
            consultation_charge
            + room_charge
            + laboratory_charge
            + medicine_charge
        ) STORED,

    payment_status VARCHAR(20) NOT NULL DEFAULT 'Unpaid',

    PRIMARY KEY (bill_id),

    CONSTRAINT fk_bill_patient
        FOREIGN KEY (patient_id)
        REFERENCES Patient(patient_id),

    CONSTRAINT fk_bill_appointment
        FOREIGN KEY (appointment_id)
        REFERENCES Appointment(appointment_id),

    CONSTRAINT fk_bill_admission
        FOREIGN KEY (admission_id)
        REFERENCES Admission(admission_id),

    CONSTRAINT chk_bill_consultation
        CHECK (consultation_charge >= 0),

    CONSTRAINT chk_bill_room
        CHECK (room_charge >= 0),

    CONSTRAINT chk_bill_lab
        CHECK (laboratory_charge >= 0),

    CONSTRAINT chk_bill_medicine
        CHECK (medicine_charge >= 0),

    CONSTRAINT chk_bill_payment_status
        CHECK (
            payment_status IN (
                'Paid',
                'Unpaid',
                'Partially Paid'
            )
        )
);

CREATE TABLE Payment (
    payment_id BIGINT AUTO_INCREMENT,
    bill_id BIGINT NOT NULL,
    payment_date DATE NOT NULL,
    payment_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    reference_number VARCHAR(100),

    PRIMARY KEY (payment_id),

    UNIQUE (reference_number),

    CONSTRAINT fk_payment_bill
        FOREIGN KEY (bill_id)
        REFERENCES Bill(bill_id),

    CONSTRAINT chk_payment_amount
        CHECK (payment_amount > 0),

    CONSTRAINT chk_payment_method
        CHECK (
            payment_method IN (
                'Cash',
                'Card',
                'Bank Transfer',
                'Online'
            )
        )
);

SHOW DATABASES;
USE SmartCare_Hospital;
SHOW TABLES;

USE SmartCare_Hospital;

INSERT INTO Department
(department_name, location, head_doctor_id)
VALUES
('Cardiology', 'Block A - 1st Floor', NULL),
('Neurology', 'Block A - 2nd Floor', NULL),
('Pediatrics', 'Block B - 1st Floor', NULL),
('Orthopedics', 'Block B - 2nd Floor', NULL),
('Radiology', 'Block C - Ground Floor', NULL);

INSERT INTO Doctor
(department_id, doctor_name, specialization, qualification,
 contact_number, consultation_fee)
VALUES
(1, 'Dr. Nimal Perera', 'Cardiologist',
 'MBBS, MD Cardiology', '0712345678', 3500.00),

(2, 'Dr. Kasun Silva', 'Neurologist',
 'MBBS, MD Neurology', '0723456789', 4000.00),

(3, 'Dr. Anjali Fernando', 'Pediatrician',
 'MBBS, MD Pediatrics', '0734567890', 3000.00),

(4, 'Dr. Saman Jayawardena', 'Orthopedic Surgeon',
 'MBBS, MS Orthopedics', '0745678901', 4500.00),

(5, 'Dr. Tharushi Perera', 'Radiologist',
 'MBBS, MD Radiology', '0756789012', 3200.00),

(1, 'Dr. Ruwan Bandara', 'Cardiologist',
 'MBBS, MD Cardiology', '0767890123', 3800.00);

UPDATE Department
SET head_doctor_id = 1
WHERE department_id = 1;

UPDATE Department
SET head_doctor_id = 2
WHERE department_id = 2;

UPDATE Department
SET head_doctor_id = 3
WHERE department_id = 3;

UPDATE Department
SET head_doctor_id = 4
WHERE department_id = 4;

UPDATE Department
SET head_doctor_id = 5
WHERE department_id = 5;


INSERT INTO Staff
(department_id, full_name, staff_role, contact_number)
VALUES
(1, 'Chamari Silva', 'Nurse', '0771111111'),
(2, 'Dilshan Perera', 'Nurse', '0772222222'),
(3, 'Kavindi Fernando', 'Nurse', '0773333333'),
(4, 'Ramesh Kumar', 'Nurse', '0774444444'),
(5, 'Ishara Senanayake', 'Lab Technician', '0775555555'),
(5, 'Nuwan Wijesinghe', 'Lab Technician', '0776666666'),
(1, 'Malith Fernando', 'Pharmacist', '0777777777'),
(3, 'Sachini Perera', 'Nurse', '0778888888');

INSERT INTO Doctor_Schedule
(doctor_id, day_of_week, start_time, end_time, availability_status)
VALUES
(1, 'Monday', '09:00:00', '13:00:00', 'Available'),
(1, 'Wednesday', '09:00:00', '13:00:00', 'Available'),

(2, 'Tuesday', '10:00:00', '14:00:00', 'Available'),
(2, 'Thursday', '10:00:00', '14:00:00', 'Available'),

(3, 'Monday', '08:00:00', '12:00:00', 'Available'),
(3, 'Friday', '08:00:00', '12:00:00', 'Available'),

(4, 'Tuesday', '09:00:00', '13:00:00', 'Available'),
(4, 'Thursday', '09:00:00', '13:00:00', 'Available'),

(5, 'Wednesday', '10:00:00', '15:00:00', 'Available'),
(5, 'Saturday', '09:00:00', '13:00:00', 'Available'),

(6, 'Monday', '14:00:00', '18:00:00', 'Available'),
(6, 'Friday', '14:00:00', '18:00:00', 'Available');


INSERT INTO Patient
(full_name, date_of_birth, gender, address,
 contact_number, blood_group, emergency_contact)
VALUES
('Amal Perera', '1995-03-15', 'Male',
 'Kandy', '0781000001', 'A+', '0719000001'),

('Nadeesha Silva', '1998-07-22', 'Female',
 'Colombo', '0781000002', 'B+', '0719000002'),

('Kasun Fernando', '1990-11-10', 'Male',
 'Kurunegala', '0781000003', 'O+', '0719000003'),

('Tharushi Jayasinghe', '2001-05-18', 'Female',
 'Dambulla', '0781000004', 'AB+', '0719000004'),

('Ravindu Bandara', '1988-09-25', 'Male',
 'Matale', '0781000005', 'A-', '0719000005'),

('Ishani Perera', '1997-12-05', 'Female',
 'Gampola', '0781000006', 'O-', '0719000006'),

('Dinesh Kumara', '1985-02-14', 'Male',
 'Negombo', '0781000007', 'B-', '0719000007'),

('Piumi Fernando', '2003-06-30', 'Female',
 'Kegalle', '0781000008', 'A+', '0719000008'),

('Sahan Wijesinghe', '1992-10-12', 'Male',
 'Galle', '0781000009', 'O+', '0719000009'),

('Madhavi Silva', '1999-01-20', 'Female',
 'Nuwara Eliya', '0781000010', 'AB-', '0719000010');


INSERT INTO Room
(room_number, room_category, daily_charge, room_status)
VALUES
('GW-101', 'General Ward', 5000.00, 'Occupied'),
('GW-102', 'General Ward', 5000.00, 'Available'),
('PR-201', 'Private Room', 10000.00, 'Occupied'),
('PR-202', 'Private Room', 10000.00, 'Available'),
('ICU-01', 'ICU', 25000.00, 'Occupied');

INSERT INTO Bed
(room_id, bed_number, availability_status)
VALUES
(1, 'B1', 'Occupied'),
(1, 'B2', 'Available'),
(2, 'B1', 'Available'),
(2, 'B2', 'Available'),
(3, 'B1', 'Occupied'),
(3, 'B2', 'Available'),
(4, 'B1', 'Available'),
(5, 'B1', 'Occupied');


INSERT INTO Appointment
(patient_id, doctor_id, appointment_date, appointment_time,
 appointment_status, consultation_room, reason)
VALUES
(1, 1, '2026-08-01', '09:00:00',
 'Completed', 'Room A101', 'Chest pain'),

(2, 1, '2026-08-01', '10:00:00',
 'Completed', 'Room A101', 'Heart checkup'),

(3, 2, '2026-08-02', '10:00:00',
 'Completed', 'Room A201', 'Headache'),

(4, 3, '2026-08-03', '08:00:00',
 'Completed', 'Room B101', 'Child fever'),

(5, 4, '2026-08-04', '09:00:00',
 'Completed', 'Room B201', 'Knee pain'),

(6, 5, '2026-08-05', '10:00:00',
 'Completed', 'Room C101', 'X-Ray examination'),

(7, 1, '2026-08-06', '11:00:00',
 'Scheduled', 'Room A101', 'Blood pressure check'),

(8, 3, '2026-08-07', '09:00:00',
 'Scheduled', 'Room B101', 'Child vaccination'),

(9, 4, '2026-08-08', '10:00:00',
 'Scheduled', 'Room B201', 'Back pain'),

(10, 5, '2026-08-09', '11:00:00',
 'Scheduled', 'Room C101', 'MRI scan'),

(1, 6, '2026-08-10', '14:00:00',
 'Scheduled', 'Room A102', 'Follow-up'),

(2, 1, '2026-08-11', '11:00:00',
 'Scheduled', 'Room A101', 'Heart follow-up'),

(3, 2, '2026-08-12', '11:00:00',
 'Scheduled', 'Room A201', 'Neurology follow-up'),

(4, 3, '2026-08-13', '09:00:00',
 'Scheduled', 'Room B101', 'Child checkup'),

(5, 4, '2026-08-14', '10:00:00',
 'Scheduled', 'Room B201', 'Orthopedic follow-up');


INSERT INTO Treatment
(patient_id, doctor_id, diagnosis, prescription,
 treatment_notes, treatment_date, medicine_charge)
VALUES
(1, 1, 'Hypertension',
 'Amlodipine 5mg once daily',
 'Regular blood pressure monitoring required',
 '2026-08-01', 1200.00),

(2, 1, 'Mild chest discomfort',
 'Aspirin 75mg once daily',
 'ECG recommended',
 '2026-08-01', 950.00),

(3, 2, 'Migraine',
 'Paracetamol 500mg as required',
 'Adequate rest recommended',
 '2026-08-02', 600.00),

(4, 3, 'Viral fever',
 'Paracetamol syrup',
 'Drink plenty of fluids',
 '2026-08-03', 750.00),

(5, 4, 'Knee pain',
 'Pain relief medication',
 'Physiotherapy recommended',
 '2026-08-04', 1500.00),

(6, 5, 'Chest X-Ray observation',
 'No medication required',
 'Follow-up recommended',
 '2026-08-05', 0.00),

(7, 1, 'High blood pressure',
 'Amlodipine 5mg',
 'Monitor BP regularly',
 '2026-08-06', 1200.00),

(8, 3, 'Routine child examination',
 'Vitamin supplement',
 'Healthy diet recommended',
 '2026-08-07', 500.00),

(9, 4, 'Lower back pain',
 'Pain relief tablets',
 'Physiotherapy recommended',
 '2026-08-08', 1300.00),

(10, 5, 'Routine scan',
 'No medication required',
 'Review scan results',
 '2026-08-09', 0.00);


INSERT INTO Laboratory_Test
(patient_id, doctor_id, technician_id, test_name,
 test_date, test_result, test_status, laboratory_charge)
VALUES
(1, 1, 5, 'Blood Test',
 '2026-08-01', 'Normal', 'Completed', 1500.00),

(2, 1, 6, 'ECG',
 '2026-08-01', 'Normal sinus rhythm', 'Completed', 2000.00),

(3, 2, 5, 'Blood Sugar',
 '2026-08-02', '98 mg/dL', 'Completed', 1200.00),

(4, 3, 6, 'Full Blood Count',
 '2026-08-03', 'Normal', 'Completed', 1800.00),

(5, 4, 5, 'X-Ray',
 '2026-08-04', 'No fracture detected', 'Completed', 3000.00),

(6, 5, 6, 'Chest X-Ray',
 '2026-08-05', 'Normal', 'Completed', 3000.00),

(7, 1, 5, 'Cholesterol Test',
 '2026-08-06', 'Slightly elevated', 'Completed', 2200.00),

(8, 3, 6, 'Blood Test',
 '2026-08-07', 'Normal', 'Completed', 1500.00),

(9, 4, 5, 'MRI Scan',
 '2026-08-08', 'Under review', 'Pending', 8000.00),

(10, 5, 6, 'CT Scan',
 '2026-08-09', 'Normal', 'Completed', 7500.00);


INSERT INTO Admission
(patient_id, bed_id, admission_date, discharge_date,
 admission_status, admission_notes)
VALUES
(1, 1, '2026-08-01', '2026-08-03',
 'Discharged', 'Observation for blood pressure'),

(2, 5, '2026-08-02', '2026-08-05',
 'Discharged', 'Cardiac observation'),

(3, 3, '2026-08-03', '2026-08-04',
 'Discharged', 'Neurological observation'),

(4, 2, '2026-08-04', '2026-08-06',
 'Discharged', 'Fever treatment'),

(5, 6, '2026-08-05', NULL,
 'Admitted', 'Orthopedic treatment'),

(6, 8, '2026-08-06', NULL,
 'Admitted', 'ICU observation');

INSERT INTO Bill
(patient_id, appointment_id, admission_id, bill_date,
 consultation_charge, room_charge, laboratory_charge, medicine_charge,
 payment_status)
VALUES
(1, 1, 1, '2026-08-03',
 3500.00, 10000.00, 1500.00, 1200.00, 'Paid'),

(2, 2, 2, '2026-08-05',
 3500.00, 30000.00, 2000.00, 950.00, 'Paid'),

(3, 3, 3, '2026-08-04',
 4000.00, 5000.00, 1200.00, 600.00, 'Unpaid'),

(4, 4, 4, '2026-08-06',
 3000.00, 10000.00, 1800.00, 750.00, 'Paid'),

(5, 5, 5, '2026-08-08',
 4500.00, 30000.00, 3000.00, 1500.00, 'Partially Paid'),

(6, 6, 6, '2026-08-08',
 3200.00, 50000.00, 3000.00, 0.00, 'Unpaid'),

(7, 7, NULL, '2026-08-06',
 3500.00, 0.00, 2200.00, 1200.00, 'Paid'),

(8, 8, NULL, '2026-08-07',
 3000.00, 0.00, 1500.00, 500.00, 'Paid'),

(9, 9, NULL, '2026-08-08',
 4500.00, 0.00, 8000.00, 1300.00, 'Unpaid'),

(10, 10, NULL, '2026-08-09',
 3200.00, 0.00, 7500.00, 0.00, 'Paid');

INSERT INTO Payment
(bill_id, payment_date, payment_amount, payment_method,
 reference_number)
VALUES
(1, '2026-08-03', 16200.00, 'Cash', 'PAY001'),

(2, '2026-08-05', 36450.00, 'Card', 'PAY002'),

(4, '2026-08-06', 5550.00, 'Online', 'PAY003'),

(5, '2026-08-08', 15000.00, 'Card', 'PAY004'),

(7, '2026-08-06', 6900.00, 'Cash', 'PAY005'),

(8, '2026-08-07', 5000.00, 'Online', 'PAY006'),

(10, '2026-08-09', 10700.00, 'Bank Transfer', 'PAY007');



-- DEPARTMENT
SELECT
    CONCAT('dep', department_id) AS department_id,
    department_name,
    location,
    CONCAT('dr', head_doctor_id) AS head_doctor_id
FROM Department;


-- DOCTOR
SELECT
    CONCAT('dr', doctor_id) AS doctor_id,
    CONCAT('dep', department_id) AS department_id,
    doctor_name,
    specialization,
    qualification,
    contact_number,
    consultation_fee
FROM Doctor;


-- STAFF
SELECT
    CONCAT('st', staff_id) AS staff_id,
    CONCAT('dep', department_id) AS department_id,
    full_name,
    staff_role,
    contact_number
FROM Staff;


-- DOCTOR SCHEDULE
SELECT
    CONCAT('sch', schedule_id) AS schedule_id,
    CONCAT('dr', doctor_id) AS doctor_id,
    day_of_week,
    start_time,
    end_time,
    availability_status
FROM Doctor_Schedule;


-- PATIENT
SELECT
    CONCAT('pt', patient_id) AS patient_id,
    full_name,
    date_of_birth,
    gender,
    address,
    contact_number,
    blood_group,
    emergency_contact
FROM Patient;


-- ROOM
SELECT
    CONCAT('rm', room_id) AS room_id,
    room_number,
    room_category,
    daily_charge,
    room_status
FROM Room;


-- BED
SELECT
    CONCAT('bd', bed_id) AS bed_id,
    CONCAT('rm', room_id) AS room_id,
    bed_number,
    availability_status
FROM Bed;


-- APPOINTMENT
SELECT
    CONCAT('app', appointment_id) AS appointment_id,
    CONCAT('pt', patient_id) AS patient_id,
    CONCAT('dr', doctor_id) AS doctor_id,
    appointment_date,
    appointment_time,
    appointment_status,
    consultation_room,
    reason
FROM Appointment;


-- TREATMENT
SELECT
    CONCAT('tr', treatment_id) AS treatment_id,
    CONCAT('pt', patient_id) AS patient_id,
    CONCAT('dr', doctor_id) AS doctor_id,
    diagnosis,
    prescription,
    treatment_notes,
    treatment_date,
    medicine_charge
FROM Treatment;


-- LABORATORY TEST
SELECT
    CONCAT('lab', lab_test_id) AS lab_test_id,
    CONCAT('pt', patient_id) AS patient_id,
    CONCAT('dr', doctor_id) AS doctor_id,
    CONCAT('st', technician_id) AS technician_id,
    test_name,
    test_date,
    test_result,
    test_status,
    laboratory_charge
FROM Laboratory_Test;


-- ADMISSION
SELECT
    CONCAT('adm', admission_id) AS admission_id,
    CONCAT('pt', patient_id) AS patient_id,
    CONCAT('bd', bed_id) AS bed_id,
    admission_date,
    discharge_date,
    admission_status,
    admission_notes
FROM Admission;


-- BILL
SELECT
    CONCAT('bill', bill_id) AS bill_id,
    CONCAT('pt', patient_id) AS patient_id,
    CONCAT('app', appointment_id) AS appointment_id,
    CONCAT('adm', admission_id) AS admission_id,
    bill_date,
    consultation_charge,
    room_charge,
    laboratory_charge,
    medicine_charge,
    total_amount,
    payment_status
FROM Bill;


SELECT
    CONCAT('pay', payment_id) AS payment_id,
    CONCAT('bill', bill_id) AS bill_id,
    payment_date,
    payment_amount,
    payment_method,
    reference_number
FROM Payment;

USE SmartCare_Hospital;




SELECT *
FROM Patient;


SELECT 
    d.department_name,
    doc.doctor_id,
    doc.doctor_name,
    doc.specialization,
    doc.consultation_fee
FROM Doctor doc
JOIN Department d
    ON doc.department_id = d.department_id
ORDER BY d.department_name;

SELECT
    a.appointment_id,
    p.full_name AS patient_name,
    doc.doctor_name,
    a.appointment_date,
    a.appointment_time,
    a.appointment_status,
    a.consultation_room,
    a.reason
FROM Appointment a
JOIN Patient p
    ON a.patient_id = p.patient_id
JOIN Doctor doc
    ON a.doctor_id = doc.doctor_id
WHERE a.doctor_id = 1
ORDER BY a.appointment_date, a.appointment_time;

SELECT
    a.admission_id,
    p.patient_id,
    p.full_name AS patient_name,
    r.room_number,
    r.room_category,
    b.bed_number,
    a.admission_date,
    a.admission_status
FROM Admission a
JOIN Patient p
    ON a.patient_id = p.patient_id
JOIN Bed b
    ON a.bed_id = b.bed_id
JOIN Room r
    ON b.room_id = r.room_id
WHERE r.room_category = 'ICU'
  AND a.admission_status = 'Admitted';
  
  SELECT
    b.bill_id,
    p.full_name AS patient_name,
    b.bill_date,
    b.total_amount,
    b.payment_status
FROM Bill b
JOIN Patient p
    ON b.patient_id = p.patient_id
WHERE b.payment_status = 'Unpaid';

SELECT
    SUM(total_amount) AS total_revenue
FROM Bill
WHERE payment_status = 'Paid';

SELECT
    doc.doctor_id,
    doc.doctor_name,
    d.department_name,
    COUNT(a.appointment_id) AS total_appointments
FROM Doctor doc
JOIN Department d
    ON doc.department_id = d.department_id
JOIN Appointment a
    ON doc.doctor_id = a.doctor_id
GROUP BY
    doc.doctor_id,
    doc.doctor_name,
    d.department_name
ORDER BY total_appointments DESC
LIMIT 1;

SELECT
    p.patient_id,
    p.full_name,
    COUNT(a.appointment_id) AS appointment_count
FROM Patient p
JOIN Appointment a
    ON p.patient_id = a.patient_id
GROUP BY
    p.patient_id,
    p.full_name
HAVING COUNT(a.appointment_id) > 1
ORDER BY appointment_count DESC;

SELECT
    l.lab_test_id,
    p.full_name AS patient_name,
    doc.doctor_name,
    l.test_name,
    l.test_date,
    l.test_result,
    l.test_status,
    l.laboratory_charge
FROM Laboratory_Test l
JOIN Patient p
    ON l.patient_id = p.patient_id
JOIN Doctor doc
    ON l.doctor_id = doc.doctor_id
WHERE l.test_status = 'Completed'
  AND l.test_date BETWEEN '2026-08-01' AND '2026-08-10'
ORDER BY l.test_date;

SELECT
    r.room_id,
    r.room_number,
    r.room_category,
    r.daily_charge,
    r.room_status,
    COUNT(b.bed_id) AS total_beds,
    SUM(
        CASE
            WHEN b.availability_status = 'Available'
            THEN 1
            ELSE 0
        END
    ) AS available_beds
FROM Room r
LEFT JOIN Bed b
    ON r.room_id = b.room_id
GROUP BY
    r.room_id,
    r.room_number,
    r.room_category,
    r.daily_charge,
    r.room_status
ORDER BY r.room_number; 


USE SmartCare_Hospital;

DROP VIEW IF EXISTS vw_patient_appointments;

CREATE VIEW vw_patient_appointments AS
SELECT
    p.patient_id,
    p.full_name AS patient_name,
    doc.doctor_name,
    d.department_name,
    a.appointment_date,
    a.appointment_time,
    a.appointment_status,
    a.consultation_room,
    a.reason
FROM Patient p
JOIN Appointment a
    ON p.patient_id = a.patient_id
JOIN Doctor doc
    ON a.doctor_id = doc.doctor_id
JOIN Department d
    ON doc.department_id = d.department_id;
    
    DROP VIEW IF EXISTS vw_unpaid_bills;

CREATE VIEW vw_unpaid_bills AS
SELECT
    b.bill_id,
    p.full_name AS patient_name,
    b.bill_date,
    b.consultation_charge,
    b.room_charge,
    b.laboratory_charge,
    b.medicine_charge,
    b.total_amount,
    b.payment_status
FROM Bill b
JOIN Patient p
    ON b.patient_id = p.patient_id
WHERE b.payment_status = 'Unpaid';

DROP PROCEDURE IF EXISTS GetPatientAppointments;

DELIMITER //

CREATE PROCEDURE GetPatientAppointments(IN p_patient_id BIGINT)
BEGIN
    SELECT
        a.appointment_id,
        p.full_name AS patient_name,
        doc.doctor_name,
        d.department_name,
        a.appointment_date,
        a.appointment_time,
        a.appointment_status,
        a.consultation_room,
        a.reason
    FROM Appointment a
    JOIN Patient p
        ON a.patient_id = p.patient_id
    JOIN Doctor doc
        ON a.doctor_id = doc.doctor_id
    JOIN Department d
        ON doc.department_id = d.department_id
    WHERE a.patient_id = p_patient_id
    ORDER BY a.appointment_date, a.appointment_time;
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS GetAvailableBeds;

DELIMITER //

CREATE PROCEDURE GetAvailableBeds()
BEGIN
    SELECT
        b.bed_id,
        r.room_number,
        r.room_category,
        b.bed_number,
        b.availability_status
    FROM Bed b
    JOIN Room r
        ON b.room_id = r.room_id
    WHERE b.availability_status = 'Available'
    ORDER BY r.room_number, b.bed_number;
END //

DELIMITER ;

DROP FUNCTION IF EXISTS GetPatientTotalBill;

DELIMITER //

CREATE FUNCTION GetPatientTotalBill(p_patient_id BIGINT)
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    DECLARE total_bill DECIMAL(10,2);

    SELECT COALESCE(SUM(total_amount), 0)
    INTO total_bill
    FROM Bill
    WHERE patient_id = p_patient_id;

    RETURN total_bill;
END //

DELIMITER ;

DROP FUNCTION IF EXISTS CountDoctorAppointments;

DELIMITER //

CREATE FUNCTION CountDoctorAppointments(p_doctor_id BIGINT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE appointment_count INT;

    SELECT COUNT(*)
    INTO appointment_count
    FROM Appointment
    WHERE doctor_id = p_doctor_id;

    RETURN appointment_count;
END //

DELIMITER ;

DROP TRIGGER IF EXISTS trg_update_bill_payment_status;

DELIMITER //

CREATE TRIGGER trg_update_bill_payment_status
AFTER INSERT ON Payment
FOR EACH ROW
BEGIN
    DECLARE total_paid DECIMAL(10,2);
    DECLARE bill_total DECIMAL(10,2);

    SELECT COALESCE(SUM(payment_amount), 0)
    INTO total_paid
    FROM Payment
    WHERE bill_id = NEW.bill_id;

    SELECT total_amount
    INTO bill_total
    FROM Bill
    WHERE bill_id = NEW.bill_id;

    IF total_paid >= bill_total THEN

        UPDATE Bill
        SET payment_status = 'Paid'
        WHERE bill_id = NEW.bill_id;

    ELSEIF total_paid > 0 THEN

        UPDATE Bill
        SET payment_status = 'Partially Paid'
        WHERE bill_id = NEW.bill_id;

    ELSE

        UPDATE Bill
        SET payment_status = 'Unpaid'
        WHERE bill_id = NEW.bill_id;

    END IF;
END //

DELIMITER ;

DROP TRIGGER IF EXISTS trg_update_bed_on_admission;

DELIMITER //

CREATE TRIGGER trg_update_bed_on_admission
AFTER INSERT ON Admission
FOR EACH ROW
BEGIN
    UPDATE Bed
    SET availability_status = 'Occupied'
    WHERE bed_id = NEW.bed_id;
END //

DELIMITER ;
