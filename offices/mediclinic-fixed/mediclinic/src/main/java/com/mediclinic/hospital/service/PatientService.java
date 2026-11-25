package com.mediclinic.hospital.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediclinic.hospital.domain.Doctor;
import com.mediclinic.hospital.domain.Patient;
import com.mediclinic.hospital.repository.AppointmentRepository;
import com.mediclinic.hospital.repository.DoctorRepository;
import com.mediclinic.hospital.repository.PatientRepository;

@Service 
public class PatientService {

@Autowired 
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private DoctorRepository doctorRepo;

@Transactional
    public String savePatient(Patient patient){

        String validationMessage = validatePatient(patient);

        if(validationMessage != null){
            return validationMessage;
        }

        if(patientRepo.existsByFirstNameAndLastName(patient.getFirstName().trim(), patient.getLastName().trim())){
            return "Patient with that name already exists";
        }

        patient.setId(null);
        patient.setFirstName(patient.getFirstName().trim());
        patient.setLastName(patient.getLastName().trim());

        patientRepo.save(patient);

        return "Patient is saved Successfully";
    }

@Transactional
    public String updatePatient(UUID id, Patient patient){

        Optional<Patient> existingPatient = patientRepo.findById(id);

        if(existingPatient.isEmpty()){
            return "No Patient found with id "+id;
        }

        String validationMessage = validatePatient(patient);

        if(validationMessage != null){
            return validationMessage;
        }

        List<Patient> patientsWithSameNames = patientRepo.findByFirstNameAndLastName(
            patient.getFirstName().trim(), patient.getLastName().trim());

        boolean nameIsTakenByAnotherPatient = patientsWithSameNames.stream()
            .anyMatch(givenPatient -> !givenPatient.getId().equals(id));

        if(nameIsTakenByAnotherPatient){
            return "Patient with that name already exists";
        }

        Patient patientToUpdate = existingPatient.get();

        patientToUpdate.setFirstName(patient.getFirstName().trim());
        patientToUpdate.setLastName(patient.getLastName().trim());
        patientToUpdate.setDateOfBirth(patient.getDateOfBirth());

        patientRepo.save(patientToUpdate);

        return "Patient is updated Successfully";
    }

@Transactional
    public String deletePatient(UUID id){

        Optional<Patient> existingPatient = patientRepo.findById(id);

        if(existingPatient.isEmpty()){
            return "No Patient found with id "+id;
        }

        if(appointmentRepo.existsByPatientId(id)){
            return "Patient is already assigned to an Appointment, delete the Appointments first";
        }

        patientRepo.delete(existingPatient.get());

        return "Patient is deleted Successfully";
    }

public Patient getPatientById(UUID id){

        return patientRepo.findById(id).orElse(null);
    }

public List<Patient> getAllPatients(){

        return patientRepo.findAll();
    }

public List<Patient> getPatientsByFirstName(String firstName){

        if(isBlank(firstName)){
            return List.of();
        }

        return patientRepo.findByFirstName(firstName.trim());
    }

public List<Patient> getPatientsByLastName(String lastName){

        if(isBlank(lastName)){
            return List.of();
        }

        return patientRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName.trim());
    }

public List<Patient> getPatientsByFullName(String firstName, String lastName){

        if(isBlank(firstName) || isBlank(lastName)){
            return List.of();
        }

        return patientRepo.findByFirstNameAndLastName(firstName.trim(), lastName.trim());
    }

private String validatePatient(Patient patient){

        if(isBlank(patient.getFirstName())){
            return "Patient first name is required";
        }

        if(isBlank(patient.getLastName())){
            return "Patient last name is required";
        }

        if(patient.getDateOfBirth() == null){
            return "Patient date of birth is required";
        }

        if(patient.getDateOfBirth().after(new Date(System.currentTimeMillis()))){
            return "Patient date of birth can not be a future date";
        }

        return null;
    }

public List<Patient> getPatientsOfDoctor(UUID doctorId){

        return appointmentRepo.findDistinctPatientsByDoctorId(doctorId);
    }

public Optional<Doctor> getDoctorById(UUID doctorId){

        return doctorRepo.findById(doctorId);
    }

public List<Patient> getFrequentPatients(long min){

        return appointmentRepo.findFrequentPatients(min);
    }

private boolean isBlank(String value){

        return value == null || value.trim().isEmpty();
    }
}
