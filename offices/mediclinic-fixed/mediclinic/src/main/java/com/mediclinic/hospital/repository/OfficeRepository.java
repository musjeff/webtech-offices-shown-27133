package com.mediclinic.hospital.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mediclinic.hospital.domain.Office;

@Repository 
public interface OfficeRepository extends  JpaRepository<Office,UUID >{

    Optional<Office> findByOfficeNumber(int officeNumber);

    Optional<Office> findByNameAndOfficeNumber(String name, int officeNumber);

    List<Office> findByNameContainingIgnoreCase(String name);

    Boolean existsByOfficeNumber(int officeNumber);

    @Query("SELECT o.name, o.officeNumber, COUNT(a) FROM Appointment a "
        + "JOIN a.doctor d JOIN d.office o "
        + "GROUP BY o.id, o.name, o.officeNumber "
        + "ORDER BY COUNT(a) DESC")
    List<Object[]> findBusiestOffice(Pageable pageable); 
}
