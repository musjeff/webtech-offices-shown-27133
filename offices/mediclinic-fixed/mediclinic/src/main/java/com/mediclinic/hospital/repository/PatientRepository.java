package com.mediclinic.hospital.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mediclinic.hospital.domain.Patient;

@Repository 
public interface PatientRepository extends  JpaRepository<Patient,UUID >{

    List<Patient> findByFirstName(String firstName);

    List<Patient> findByLastName(String lastName);

    List<Patient> findByFirstNameAndLastName(String firstName, String lastName);

    Boolean existsByFirstNameAndLastName(String firstName, String lastName);

    List<Patient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName); 
}
