package com.mediclinic.hospital.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mediclinic.hospital.domain.Specialization;

@Repository 
public interface SpecializationRepository extends  JpaRepository<Specialization,UUID >{

    Optional<Specialization> findByName(String name);

    List<Specialization> findByNameContainingIgnoreCase(String name);

    Boolean existsByName(String name);

    @Query("SELECT s FROM Specialization s WHERE s.doctors IS EMPTY")
    List<Specialization> findUnusedSpecializations(); 
}
