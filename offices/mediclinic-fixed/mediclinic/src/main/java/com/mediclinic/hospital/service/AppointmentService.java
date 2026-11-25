package com.mediclinic.hospital.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediclinic.hospital.domain.Appointment;
import com.mediclinic.hospital.domain.AppointmentStatus;
import com.mediclinic.hospital.domain.Doctor;
import com.mediclinic.hospital.domain.Patient;
import com.mediclinic.hospital.repository.AppointmentRepository;
import com.mediclinic.hospital.repository.DoctorRepository;
import com.mediclinic.hospital.repository.PatientRepository;

@Service 
public class AppointmentService {

@Autowired 
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

@Transactional
    public String saveAppointment(Appointment appointment){

        if(appointment.getStatus() == null){
            appointment.setStatus(AppointmentStatus.SCHEDULED);
        }

        String validationMessage = validateAppointment(appointment);

        if(validationMessage != null){
            return validationMessage;
        }

        String referenceMessage = checkGivenReferences(appointment);

        if(referenceMessage != null){
            return referenceMessage;
        }

        if(appointment.getStatus() == null){
            appointment.setStatus(AppointmentStatus.SCHEDULED);
        }

        if(appointment.getStatus() == null){
            appointment.setStatus(AppointmentStatus.SCHEDULED);
        }

        boolean doctorIsBooked = appointmentRepo.existsByDoctorIdAndAppointmentDateAndStatusNot(
            appointment.getDoctor().getId(),
            appointment.getAppointmentDate(),
            AppointmentStatus.CANCELLED
        );

        if(doctorIsBooked){
            return "Doctor is already booked on that date";
        }

        appointment.setId(null);
        appointment.setReason(appointment.getReason().trim());
        appointment.setPatient(resolvePatient(appointment.getPatient()));
        appointment.setDoctor(resolveDoctor(appointment.getDoctor()));

        appointmentRepo.save(appointment);

        return "Appointment is saved Successfully";
    }

@Transactional
    public String updateAppointment(UUID id, Appointment appointment){

        Optional<Appointment> existingAppointment = appointmentRepo.findById(id);

        if(existingAppointment.isEmpty()){
            return "No Appointment found with id "+id;
        }

        String validationMessage = validateAppointment(appointment);

        if(validationMessage != null){
            return validationMessage;
        }

        String referenceMessage = checkGivenReferences(appointment);

        if(referenceMessage != null){
            return referenceMessage;
        }

        Appointment appointmentToUpdate = existingAppointment.get();

        appointmentToUpdate.setAppointmentDate(appointment.getAppointmentDate());
        appointmentToUpdate.setReason(isBlank(appointment.getReason()) ? null : appointment.getReason().trim());
        appointmentToUpdate.setStatus(appointment.getStatus());
        appointmentToUpdate.setPatient(resolvePatient(appointment.getPatient()));
        appointmentToUpdate.setDoctor(resolveDoctor(appointment.getDoctor()));

        appointmentRepo.save(appointmentToUpdate);

        return "Appointment is updated Successfully";
    }

@Transactional
    public String deleteAppointment(UUID id){

        Optional<Appointment> existingAppointment = appointmentRepo.findById(id);

        if(existingAppointment.isEmpty()){
            return "No Appointment found with id "+id;
        }

        appointmentRepo.delete(existingAppointment.get());

        return "Appointment is deleted Successfully";
    }

public Appointment getAppointmentById(UUID id){

        return appointmentRepo.findById(id).orElse(null);
    }

public List<Appointment> getAllAppointments(){

        return appointmentRepo.findAll();
    }

public List<Appointment> getAppointmentsByStatus(AppointmentStatus status){

        if(status == null){
            return List.of();
        }

        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

public List<Appointment> getAppointmentsByPatient(UUID patientId){

        if(patientRepo.findById(patientId).isEmpty()){
            return List.of();
        }

        return appointmentRepo.findByPatientId(patientId);
    }

public List<Appointment> getAppointmentsByDoctor(UUID doctorId){

        if(doctorRepo.findById(doctorId).isEmpty()){
            return List.of();
        }

        return appointmentRepo.findByDoctorId(doctorId);
    }

private String validateAppointment(Appointment appointment){

        if(appointment.getAppointmentDate() == null){
            return "Appointment date is required";
        }

        if(appointment.getStatus() == null){
            return "Appointment status is required";
        }

        if(isBlank(appointment.getReason())){
            return "Appointment reason is required";
        }

        if(appointment.getPatient() == null){
            return "Appointment patient is required";
        }

        if(appointment.getDoctor() == null){
            return "Appointment doctor is required";
        }

        return null;
    }

private String checkGivenReferences(Appointment appointment){

        if(appointment.getPatient().getId() == null){
            return "Patient id is required for an Appointment";
        }

        if(patientRepo.findById(appointment.getPatient().getId()).isEmpty()){
            return "No Patient found with id "+appointment.getPatient().getId();
        }

        if(appointment.getDoctor().getId() == null){
            return "Doctor id is required for an Appointment";
        }

        if(doctorRepo.findById(appointment.getDoctor().getId()).isEmpty()){
            return "No Doctor found with id "+appointment.getDoctor().getId();
        }

        return null;
    }

private Patient resolvePatient(Patient patient){

        return patientRepo.findById(patient.getId()).orElse(null);
    }

private Doctor resolveDoctor(Doctor doctor){

        return doctorRepo.findById(doctor.getId()).orElse(null);
    }

public List<Appointment> getAppointmentsBetween(LocalDate start, LocalDate end){

        return appointmentRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

public List<Object[]> getAppointmentsCountByStatus(){

        return appointmentRepo.countAppointmentsByStatus();
    }

public Page<Appointment> getAppointmentsPage(Pageable pageable){

        return appointmentRepo.findAll(pageable);
    } 

@Transactional
    public String cancelAppointmentsOnDate(UUID doctorId, LocalDate date){

        int cancelledAppointments = appointmentRepo.cancelAppointmentsOnDate(doctorId, date);

        return cancelledAppointments + " appointments cancelled";
    }

@Transactional
    public String deleteCancelledAppointmentsBefore(LocalDate date){

        int deletedAppointments = appointmentRepo.deleteCancelledAppointmentsBefore(date);

        return deletedAppointments + " appointments deleted";
    }

private boolean isBlank(String value){

        return value == null || value.trim().isEmpty();
    }
}
