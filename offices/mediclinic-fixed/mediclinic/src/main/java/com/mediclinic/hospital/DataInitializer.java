package com.mediclinic.hospital;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

import com.mediclinic.hospital.domain.Appointment;
import com.mediclinic.hospital.domain.AppointmentStatus;
import com.mediclinic.hospital.domain.Doctor;
import com.mediclinic.hospital.domain.Office;
import com.mediclinic.hospital.domain.Patient;
import com.mediclinic.hospital.domain.Specialization;
import com.mediclinic.hospital.repository.AppointmentRepository;
import com.mediclinic.hospital.repository.DoctorRepository;
import com.mediclinic.hospital.repository.OfficeRepository;
import com.mediclinic.hospital.repository.PatientRepository;
import com.mediclinic.hospital.repository.SpecializationRepository;

@Configuration
public class DataInitializer {

    @Autowired
    private SpecializationRepository specializationRepo;

    @Autowired
    private OfficeRepository officeRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Bean
    CommandLineRunner loadData(){

return args -> transactionTemplate.execute(status -> {

            if(specializationRepo.count() > 0){
                return null;
            }

            seedSpecializations();
            seedOffices();
            seedDoctors();
            seedPatients();
            seedAppointments();

            return null;
        });
    }

    void seedSpecializations(){

        saveSpecialization("Cardiology");
        saveSpecialization("Pediatrics");
        saveSpecialization("Dermatology");
        saveSpecialization("Neurology");
    }

    void saveSpecialization(String name){

        Specialization specialization = new Specialization();
        specialization.setName(name);
        specializationRepo.save(specialization);
    }

    void seedOffices(){

        saveOffice("Kigali Clinic", "Kigali Clinic", 101);
        saveOffice("Kigali Clinic", "Kigali Clinic", 102);
        saveOffice("Kigali Clinic", "Kigali Clinic", 103);
    }

    void saveOffice(String name, String buildingName, int officeNumber){

        Office office = new Office();
        office.setName(name);
        office.setBuildingName(buildingName);
        office.setOfficeNumber(officeNumber);
        officeRepo.save(office);
    }

    void seedDoctors(){

        Specialization cardiology = specializationRepo.findByName("Cardiology").get();
        Specialization pediatrics = specializationRepo.findByName("Pediatrics").get();
        Specialization dermatology = specializationRepo.findByName("Dermatology").get();

        Office office101 = officeRepo.findByOfficeNumber(101).get();
        Office office102 = officeRepo.findByOfficeNumber(102).get();

        Doctor aline = new Doctor();
        aline.setFirstName("Aline");
        aline.setLastName("Mukamana");
        aline.setDateOfBirth(Date.valueOf("1984-06-12"));
        aline.setOffice(office101);
        doctorRepo.save(aline);

        Doctor emmanuel = new Doctor();
        emmanuel.setFirstName("Emmanuel");
        emmanuel.setLastName("Nshimiyimana");
        emmanuel.setDateOfBirth(Date.valueOf("1979-02-28"));
        emmanuel.setOffice(office102);
        doctorRepo.save(emmanuel);

        Doctor grace = new Doctor();
        grace.setFirstName("Grace");
        grace.setLastName("Uwase");
        grace.setDateOfBirth(Date.valueOf("1991-11-03"));
        doctorRepo.save(grace);

        cardiology.getDoctors().add(aline);
        pediatrics.getDoctors().add(aline);
        dermatology.getDoctors().add(emmanuel);

        specializationRepo.save(cardiology);
        specializationRepo.save(pediatrics);
        specializationRepo.save(dermatology);
    }

    void seedPatients(){

        savePatient("Divine", "Uwase", "1998-04-09");
        savePatient("Yves", "Uwase", "1995-09-21");
        savePatient("Sandrine", "Mukandayisenga", "1987-01-15");
        savePatient("Providence", "Nshimirimana", "1993-07-30");
        savePatient("Daphine", "Iradukunda", "2000-12-05");
    }

    void savePatient(String firstName, String lastName, String dateOfBirth){

        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setDateOfBirth(Date.valueOf(dateOfBirth));
        patientRepo.save(patient);
    }

    void seedAppointments(){

        Doctor aline = doctorRepo.findByFirstNameAndLastName("Aline", "Mukamana").get(0);
        Doctor emmanuel = doctorRepo.findByFirstNameAndLastName("Emmanuel", "Nshimiyimana").get(0);
        Doctor grace = doctorRepo.findByFirstNameAndLastName("Grace", "Uwase").get(0);

        Patient divine = patientRepo.findByFirstNameAndLastName("Divine", "Uwase").get(0);
        Patient yves = patientRepo.findByFirstNameAndLastName("Yves", "Uwase").get(0);
        Patient sandrine = patientRepo.findByFirstNameAndLastName("Sandrine", "Mukandayisenga").get(0);
        Patient providence = patientRepo.findByFirstNameAndLastName("Providence", "Nshimirimana").get(0);
        Patient daphine = patientRepo.findByFirstNameAndLastName("Daphine", "Iradukunda").get(0);

        saveAppointment(divine, aline, "2026-10-05", "Malaria check up", AppointmentStatus.SCHEDULED);
        saveAppointment(divine, emmanuel, "2026-10-12", "Skin rash consultation", AppointmentStatus.CONFIRMED);
        saveAppointment(divine, grace, "2026-11-03", "General check up", AppointmentStatus.COMPLETED);
        saveAppointment(yves, aline, "2026-10-20", "High blood pressure follow up", AppointmentStatus.SCHEDULED);
        saveAppointment(yves, aline, "2026-10-20", "Chest pain", AppointmentStatus.CANCELLED);
        saveAppointment(sandrine, emmanuel, "2026-10-20", "Acne treatment", AppointmentStatus.CONFIRMED);
        saveAppointment(providence, aline, "2026-11-10", "Heart check up", AppointmentStatus.SCHEDULED);
        saveAppointment(daphine, grace, "2026-11-17", "Child vaccination", AppointmentStatus.COMPLETED);
        saveAppointment(sandrine, aline, "2026-11-24", "Headache and fever", AppointmentStatus.CANCELLED);
        saveAppointment(daphine, emmanuel, "2026-10-28", "Antenatal check up", AppointmentStatus.SCHEDULED);
    }

    void saveAppointment(Patient patient, Doctor doctor, String appointmentDate, String reason, AppointmentStatus status){

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(LocalDate.parse(appointmentDate));
        appointment.setReason(reason);
        appointment.setStatus(status);
        appointmentRepo.save(appointment);
    }
}
