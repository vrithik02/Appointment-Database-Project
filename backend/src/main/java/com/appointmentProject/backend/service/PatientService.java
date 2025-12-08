/******************************************************************************************************************
 * PatientService.java
 *
 *          Provides the CRUD operations and certain find queries for getting the patient data.
 *
 * @author Matthew Kiyono
 * @since 12/4/2025
 * @version 1.1
 ******************************************************************************************************************/
package com.appointmentProject.backend.service;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.Patient;
import com.appointmentProject.backend.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepo;

    // INSERT
    public Patient addPatient(Patient p) {

        // 1. Unique first + last name
        List<Patient> existing = patientRepo.findByLastName(p.getLastName());
        for (Patient x : existing) {
            if (x.getFirstName().equalsIgnoreCase(p.getFirstName())) {
                throw new IllegalArgumentException("Duplicate patient name.");
            }
        }

        // 2. Age ≥ 0
        if (p.getAge() < 0)
            throw new IllegalArgumentException("Age must be ≥ 0");

        // 3. Height/Weight > 0
        if (p.getHeight() <= 0 || p.getWeight() <= 0)
            throw new IllegalArgumentException("Invalid height/weight");

        return patientRepo.save(p);
    }

    // DELETE
    public void removePatient(Patient patient) {
        patientRepo.deleteById(patient.getId());
    }

    // UPDATE
    public Patient updatePatient(Patient update) {
        Optional<Patient> exists = patientRepo.findById(update.getId());

        if (exists.isEmpty()) {
            throw new RecordNotFoundException(
                    "Patient with ID " + update.getId() + " was not found."
            );
        }

        Patient current = exists.get();

        current.setFirstName(update.getFirstName());
        current.setLastName(update.getLastName());
        current.setPhone(update.getPhone());
        current.setEmail(update.getEmail());

        current.setDoB(update.getDoB());
        current.setAge(update.getAge());
        current.setWeight(update.getWeight());
        current.setHeight(update.getHeight());
        current.setGender(update.getGender());
        current.setInsuranceId(update.getInsuranceId());
        current.setEmergencyContactId(update.getEmergencyContactId());

        return patientRepo.save(current);
    }

    // SELECT ALL
    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    // SELECT BY ID
    public Optional<Patient> getPatientById(int id) {
        return patientRepo.findById(id);
    }

    // QUERIES
    public List<Patient> getByLastName(String lastName) {
        return patientRepo.findByLastName(lastName);
    }

    public List<Patient> getByPhone(String phone) {
        return patientRepo.findByPhone(phone);
    }

    public List<Patient> getByEmail(String email) {
        return patientRepo.findByEmail(email);
    }

    public List<Patient> getByGender(String gender) {
        return patientRepo.findByGender(gender);
    }

    public List<Patient> getByInsuranceId(Integer insuranceId) {
        return patientRepo.findByInsuranceId(insuranceId);
    }
}
