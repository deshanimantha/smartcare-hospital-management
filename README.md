# SmartCare Hospital Management System - API Testing Guide

Base URL for all requests: `http://localhost:8080`



## 1. Patients API (`/api/patients`)
* **Get All Patients:** 
  *  `GET` URL: `http://localhost:8080/api/patients`

* **Get Patient By ID:** 
  `GET` URL: `http://localhost:8080/api/patients/{id}`

Register Patient (POST):** 
`POST`  URL: `http://localhost:8080/api/patients`
  * Body (JSON): {ex}

* **Update Patient (PUT):** 
  `PUT` URL: `http://localhost:8080/api/patients/{id}`
  * Body (JSON):{ex}


 * **Delete Patient:** 
  * `DELETE` URL: `http://localhost:8080/api/patients/{id}`
    


## 2. Departments API (`/api/departments`)
* **Get All Departments:** 
  * `GET`  URL: `http://localhost:8080/api/departments`

* **Delete Department:** 
  * `DELETE`  URL: `http://localhost:8080/api/departments/{id}`

* **Create Department (POST):** 
  * `POST` URL: `http://localhost:8080/api/departments`
  * Body (JSON):{ex}




## 3. Other Controllers (GET & DELETE Only)

* **Doctors:** 
  * `GET` http://localhost:8080/api/doctors
  * `DELETE` http://localhost:8080/api/doctors/{id}

    
* **Appointments:** 
  * `GET` http://localhost:8080/api/appointments
  * `DELETE` http://localhost:8080/api/appointments/{id}


* **Admissions:** 
  * `GET` http://localhost:8080/api/admissions
  * `DELETE` http://localhost:8080/api/admissions/{id}


* **Beds:** 
  * `GET` http://localhost:8080/api/beds
  * `DELETE` http://localhost:8080/api/beds/{id}


* **Bills:** 
  * `GET` http://localhost:8080/api/bills
  * `DELETE` http://localhost:8080/api/bills/{id}


* **Rooms:** 
  * `GET` http://localhost:8080/api/rooms
  * `DELETE` http://localhost:8080/api/rooms/{id}


* **Staff:** 
  * `GET` http://localhost:8080/api/staff
  * `DELETE` http://localhost:8080/api/staff/{id}


* **Treatments:** 
  * `GET` http://localhost:8080/api/treatments
  * `DELETE` http://localhost:8080/api/treatments/{id}


* **Laboratory Tests:** 
  * `GET` http://localhost:8080/api/laboratory-tests
  * `DELETE` http://localhost:8080/api/laboratory-tests/{id}


* **Payments:** 
  * `GET` http://localhost:8080/api/payments
  * `GET` http://localhost:8080/api/payments/{id}


* **Doctor Schedules:** 
  * `GET` http://localhost:8080/api/doctor-schedules
  * `DELETE` http://localhost:8080/api/doctor-schedules/{id} 
