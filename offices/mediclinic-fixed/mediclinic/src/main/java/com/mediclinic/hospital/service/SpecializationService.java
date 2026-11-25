package com.mediclinic.hospital.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediclinic.hospital.domain.Specialization;
import com.mediclinic.hospital.repository.DoctorRepository;
import com.mediclinic.hospital.repository.SpecializationRepository;

@Service 
public class SpecializationService {

@Autowired 
    private SpecializationRepository specializationRepo;

    @Autowired
    private DoctorRepository doctorRepo;

@Transactional
    public String saveSpecialization(Specialization specialization){

        if(isBlank(specialization.getName())){
            return "Specialization name is required";
        }

        if(specializationRepo.existsByName(specialization.getName().trim())){
            return "Specialization with that name "+specialization.getName().trim()+ " already exists";
        }

        specialization.setId(null);
        specialization.setName(specialization.getName().trim());

        specializationRepo.save(specialization);

        return "Specialization is saved Successfully";
    }

@Transactional
    public String updateSpecialization(UUID id, Specialization specialization){

        Optional<Specialization> existingSpecialization = specializationRepo.findById(id);

        if(existingSpecialization.isEmpty()){
            return "No Specialization found with id "+id;
        }

        if(isBlank(specialization.getName())){
            return "Specialization name is required";
        }

        Optional<Specialization> specializationWithThatName = specializationRepo.findByName(specialization.getName().trim());

        if(specializationWithThatName.isPresent() && !specializationWithThatName.get().getId().equals(id)){
            return "Specialization with that name "+specialization.getName().trim()+ " already exists";
        }

        Specialization specializationToUpdate = existingSpecialization.get();

        specializationToUpdate.setName(specialization.getName().trim());

        specializationRepo.save(specializationToUpdate);

        return "Specialization is updated Successfully";
    }

@Transactional
    public String deleteSpecialization(UUID id){

        Optional<Specialization> existingSpecialization = specializationRepo.findById(id);

        if(existingSpecialization.isEmpty()){
            return "No Specialization found with id "+id;
        }

        if(!doctorRepo.findBySpecializationsId(id).isEmpty()){
            return "Specialization is already assigned to a Doctor, remove it from the Doctors first";
        }

        specializationRepo.delete(existingSpecialization.get());

        return "Specialization is deleted Successfully";
    }

public Specialization getSpecializationById(UUID id){

        return specializationRepo.findById(id).orElse(null);
    }

public List<Specialization> getAllSpecializations(){

        return specializationRepo.findAll();
    }

public List<Specialization> getSpecializationsByName(String name){

        if(isBlank(name)){
            return List.of();
        }

        return specializationRepo.findByNameContainingIgnoreCase(name.trim());
    }

public List<Specialization> getUnusedSpecializations(){

        return specializationRepo.findUnusedSpecializations();
    }

private boolean isBlank(String value){

        return value == null || value.trim().isEmpty();
    }
}
