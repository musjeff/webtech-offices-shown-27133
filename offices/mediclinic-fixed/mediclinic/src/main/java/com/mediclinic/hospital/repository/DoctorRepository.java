package com.mediclinic.hospital.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mediclinic.hospital.domain.Doctor;

@Repository 
public interface DoctorRepository extends  JpaRepository<Doctor,UUID >{

    List<Doctor> findByFirstName(String firstName);

    List<Doctor> findByLastName(String lastName);

    List<Doctor> findByFirstNameAndLastName(String firstName, String lastName);

    List<Doctor> findBySpecializationsId(UUID specializationId);

    Optional<Doctor> findByOfficeId(UUID officeId);

    Optional<Doctor> findByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, Date dateOfBirth);

    @Query("SELECT d FROM Doctor d JOIN d.specializations s WHERE LOWER(s.name) = LOWER(:name)")
    List<Doctor> findDoctorsBySpecializationName(@Param("name") String name); 

    @Query("SELECT d FROM Doctor d WHERE d.office IS NULL ORDER BY d.lastName ASC")
    List<Doctor> findDoctorsWithoutOffice(); 
}
