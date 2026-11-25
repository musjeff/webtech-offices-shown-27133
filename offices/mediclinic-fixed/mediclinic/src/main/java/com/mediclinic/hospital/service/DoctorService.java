package com.mediclinic.hospital.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediclinic.hospital.domain.Doctor;
import com.mediclinic.hospital.domain.Office;
import com.mediclinic.hospital.domain.Specialization;
import com.mediclinic.hospital.repository.AppointmentRepository;
import com.mediclinic.hospital.repository.DoctorRepository;
import com.mediclinic.hospital.repository.OfficeRepository;
import com.mediclinic.hospital.repository.SpecializationRepository;

@Service 
public class DoctorService {

@Autowired 
    private DoctorRepository doctorRepo;

    @Autowired
    private OfficeRepository officeRepo;

    @Autowired
    private SpecializationRepository specializationRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

@Transactional
    public String saveDoctor(Doctor doctor){

        String validationMessage = validateDoctor(doctor);

        if(validationMessage != null){
            return validationMessage;
        }

        if(doctorRepo.findByFirstNameAndLastNameAndDateOfBirth(
            doctor.getFirstName().trim(), doctor.getLastName().trim(), doctor.getDateOfBirth()).isPresent()){
            return "Doctor with that name and date of birth already exists";
        }

        String officeMessage = checkGivenOffice(doctor.getOffice(), null);

        if(officeMessage != null){
            return officeMessage;
        }

        List<Specialization> specializations = resolveSpecializations(doctor.getSpecializations());

        if(specializations == null){
            return "No Specialization found for one of the given specialization ids";
        }

        doctor.setId(null);
        doctor.setFirstName(doctor.getFirstName().trim());
        doctor.setLastName(doctor.getLastName().trim());
        doctor.setOffice(resolveOffice(doctor.getOffice()));

        Doctor doctorToSave = doctorRepo.save(doctor);

        linkSpecializations(doctorToSave, specializations);

        return "Doctor is saved Successfully";
    }

@Transactional
    public String updateDoctor(UUID id, Doctor doctor){

        Optional<Doctor> existingDoctor = doctorRepo.findById(id);

        if(existingDoctor.isEmpty()){
            return "No Doctor found with id "+id;
        }

        String validationMessage = validateDoctor(doctor);

        if(validationMessage != null){
            return validationMessage;
        }

        Optional<Doctor> doctorWithSameNames = doctorRepo.findByFirstNameAndLastNameAndDateOfBirth(
            doctor.getFirstName().trim(), doctor.getLastName().trim(), doctor.getDateOfBirth());

        if(doctorWithSameNames.isPresent() && !doctorWithSameNames.get().getId().equals(id)){
            return "Doctor with that name and date of birth already exists";
        }

        String officeMessage = checkGivenOffice(doctor.getOffice(), id);

        if(officeMessage != null){
            return officeMessage;
        }

        List<Specialization> specializations = resolveSpecializations(doctor.getSpecializations());

        if(specializations == null){
            return "No Specialization found for one of the given specialization ids";
        }

        Doctor doctorToUpdate = existingDoctor.get();

        doctorToUpdate.setFirstName(doctor.getFirstName().trim());
        doctorToUpdate.setLastName(doctor.getLastName().trim());
        doctorToUpdate.setDateOfBirth(doctor.getDateOfBirth());
        doctorToUpdate.setOffice(resolveOffice(doctor.getOffice()));

        linkSpecializations(doctorToUpdate, specializations);

        doctorRepo.save(doctorToUpdate);

        return "Doctor is updated Successfully";
    }

@Transactional
    public String deleteDoctor(UUID id){

        Optional<Doctor> existingDoctor = doctorRepo.findById(id);

        if(existingDoctor.isEmpty()){
            return "No Doctor found with id "+id;
        }

        if(appointmentRepo.existsByDoctorId(id)){
            return "Doctor is already assigned to an Appointment, delete the Appointments first";
        }

        Doctor doctorToDelete = existingDoctor.get();

        if(doctorToDelete.getOffice() != null){
            doctorToDelete.getOffice().setDoctor(null);
        }

        linkSpecializations(doctorToDelete, new ArrayList<>());

        doctorRepo.delete(doctorToDelete);

        return "Doctor is deleted Successfully";
    }

public Doctor getDoctorById(UUID id){

        return doctorRepo.findById(id).orElse(null);
    }

public List<Doctor> getAllDoctors(){

        return doctorRepo.findAll();
    }

public List<Doctor> getDoctorsByFirstName(String firstName){

        if(isBlank(firstName)){
            return List.of();
        }

        return doctorRepo.findByFirstName(firstName.trim());
    }

public List<Doctor> getDoctorsByLastName(String lastName){

        if(isBlank(lastName)){
            return List.of();
        }

        return doctorRepo.findByLastName(lastName.trim());
    }

public List<Doctor> getDoctorsByFullName(String firstName, String lastName){

        if(isBlank(firstName) || isBlank(lastName)){
            return List.of();
        }

        return doctorRepo.findByFirstNameAndLastName(firstName.trim(), lastName.trim());
    }

public List<Doctor> getDoctorsBySpecialization(UUID specializationId){

        return doctorRepo.findBySpecializationsId(specializationId);
    }

private String validateDoctor(Doctor doctor){

        if(isBlank(doctor.getFirstName())){
            return "Doctor first name is required";
        }

        if(isBlank(doctor.getLastName())){
            return "Doctor last name is required";
        }

        if(doctor.getDateOfBirth() == null){
            return "Doctor date of birth is required";
        }

        if(doctor.getDateOfBirth().after(new Date(System.currentTimeMillis()))){
            return "Doctor date of birth can not be a future date";
        }

        return null;
    }

private String checkGivenOffice(Office office, UUID doctorId){

        if(office == null){
            return null;
        }

        if(office.getId() == null){
            return "Office id is required when an Office is given to a Doctor";
        }

        if(officeRepo.findById(office.getId()).isEmpty()){
            return "No Office found with id "+office.getId();
        }

        Optional<Doctor> doctorHoldingThatOffice = doctorRepo.findByOfficeId(office.getId());

        if(doctorHoldingThatOffice.isPresent() && !doctorHoldingThatOffice.get().getId().equals(doctorId)){
            return "Office is already assigned to another Doctor";
        }

        return null;
    }

private Office resolveOffice(Office office){

        if(office == null){
            return null;
        }

        return officeRepo.findById(office.getId()).orElse(null);
    }

private void linkSpecializations(Doctor doctor, List<Specialization> specializations){

        List<Specialization> currentSpecializations = new ArrayList<>(doctor.getSpecializations());

        for(Specialization currentSpecialization : currentSpecializations){

            if(!specializations.contains(currentSpecialization)){
                currentSpecialization.getDoctors().remove(doctor);
                specializationRepo.save(currentSpecialization);
            }
        }

        for(Specialization specialization : specializations){

            if(!specialization.getDoctors().contains(doctor)){
                specialization.getDoctors().add(doctor);
                specializationRepo.save(specialization);
            }
        }
    }

private List<Specialization> resolveSpecializations(List<Specialization> givenSpecializations){

        List<Specialization> resolvedSpecializations = new ArrayList<>();

        if(givenSpecializations == null){
            return resolvedSpecializations;
        }

        for (Specialization givenSpecialization : givenSpecializations) {

            if(givenSpecialization == null || givenSpecialization.getId() == null){
                return null;
            }

            Optional<Specialization> specialization = specializationRepo.findById(givenSpecialization.getId());

            if(specialization.isEmpty()){
                return null;
            }

            resolvedSpecializations.add(specialization.get());
        }

        return resolvedSpecializations;
    }

public List<Doctor> getDoctorsBySpecializationName(String name){

        if(isBlank(name)){
            return List.of();
        }

        return doctorRepo.findDoctorsBySpecializationName(name.trim());
    }

public List<Doctor> getDoctorsWithoutOffice(){

        return doctorRepo.findDoctorsWithoutOffice();
    }

private boolean isBlank(String value){

        return value == null || value.trim().isEmpty();
    }
}
