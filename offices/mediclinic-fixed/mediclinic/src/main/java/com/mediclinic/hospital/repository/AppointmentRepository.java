package com.mediclinic.hospital.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mediclinic.hospital.domain.Appointment;
import com.mediclinic.hospital.domain.AppointmentStatus;
import com.mediclinic.hospital.domain.Patient;

@Repository 
public interface AppointmentRepository extends  JpaRepository<Appointment,UUID >{

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByPatientId(UUID patientId);

    List<Appointment> findByDoctorId(UUID doctorId);

    Boolean existsByPatientId(UUID patientId);

    Boolean existsByDoctorId(UUID doctorId);

    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status); 

    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(LocalDate start, LocalDate end); 

    Boolean existsByDoctorIdAndAppointmentDateAndStatusNot(UUID doctorId, LocalDate appointmentDate, AppointmentStatus status); 

    @Query("SELECT DISTINCT a.patient FROM Appointment a WHERE a.doctor.id = :doctorId")
    List<Patient> findDistinctPatientsByDoctorId(@Param("doctorId") UUID doctorId); 

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countAppointmentsByStatus(); 

    @Query("SELECT a.patient FROM Appointment a GROUP BY a.patient HAVING COUNT(a) >= :min ORDER BY COUNT(a) DESC")
    List<Patient> findFrequentPatients(@Param("min") long min); 

    @Modifying
    @Query("UPDATE Appointment a SET a.status = com.mediclinic.hospital.domain.AppointmentStatus.CANCELLED "
        + "WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date "
        + "AND a.status <> com.mediclinic.hospital.domain.AppointmentStatus.COMPLETED")
    int cancelAppointmentsOnDate(@Param("doctorId") UUID doctorId, @Param("date") LocalDate date); 

    @Modifying
    @Query("DELETE FROM Appointment a "
        + "WHERE a.status = com.mediclinic.hospital.domain.AppointmentStatus.CANCELLED "
        + "AND a.appointmentDate < :date")
    int deleteCancelledAppointmentsBefore(@Param("date") LocalDate date); 
}
