package com.mediclinic.hospital.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediclinic.hospital.domain.Appointment;
import com.mediclinic.hospital.domain.AppointmentStatus;
import com.mediclinic.hospital.service.AppointmentService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController 
@RequestMapping (value = "/api/appointments")
public class AppointmentController {

@Autowired 
    private AppointmentService appointmentService;

@PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?>  saveAppointment(@RequestBody  Appointment appointment){

        String returnedMessage = appointmentService.saveAppointment(appointment);

        if(returnedMessage.equalsIgnoreCase("Appointment is saved Successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        }else if(returnedMessage.equalsIgnoreCase("Doctor is already booked on that date")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }else if(returnedMessage.startsWith("No ")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else if(returnedMessage.contains("already")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
    }

@PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateAppointment(@PathVariable UUID id, @RequestBody Appointment appointment){

        String returnedMessage = appointmentService.updateAppointment(id, appointment);

        if(returnedMessage.equalsIgnoreCase("Appointment is updated Successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.startsWith("No ")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else if(returnedMessage.contains("already")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
    }

@DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAppointment(@PathVariable UUID id){

        String returnedMessage = appointmentService.deleteAppointment(id);

        if(returnedMessage.equalsIgnoreCase("Appointment is deleted Successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.startsWith("No ")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else if(returnedMessage.contains("already")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
    }

@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentById(@PathVariable UUID id){

        Appointment appointment = appointmentService.getAppointmentById(id);

        if (appointment == null) {
            return new ResponseEntity<>("No Appointment found with id "+id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getAllAppointments(){

        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
    }

@GetMapping(value = "/by-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(@RequestParam AppointmentStatus status){

        return new ResponseEntity<>(appointmentService.getAppointmentsByStatus(status), HttpStatus.OK);
    }

@GetMapping(value = "/find/patient/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(@PathVariable UUID patientId){

        return new ResponseEntity<>(appointmentService.getAppointmentsByPatient(patientId), HttpStatus.OK);
    }

@GetMapping(value = "/find/doctor/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(@PathVariable UUID doctorId){

        return new ResponseEntity<>(appointmentService.getAppointmentsByDoctor(doctorId), HttpStatus.OK);
    }

@GetMapping(value = "/between", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getAppointmentsBetween(@RequestParam String start, @RequestParam String end){

        List<Appointment> appointments = appointmentService.getAppointmentsBetween(
            LocalDate.parse(start),
            LocalDate.parse(end)
        );

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

@GetMapping(value = "/stats/by-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Object[]>> getAppointmentsCountByStatus(){

        return new ResponseEntity<>(appointmentService.getAppointmentsCountByStatus(), HttpStatus.OK);
    }

@PatchMapping(value = "/cancel-day", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelAppointmentsOnDate(@RequestParam UUID doctorId, @RequestParam String date){

        String returnedMessage = appointmentService.cancelAppointmentsOnDate(doctorId, LocalDate.parse(date));

        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

@GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Appointment>> getAppointmentsPage(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "appointmentDate,asc") String sort
    ){

        String[] sortParts = sort.split(",");

        Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParts[0]));

        return new ResponseEntity<>(appointmentService.getAppointmentsPage(pageable), HttpStatus.OK);
    }

@DeleteMapping(value = "/cancelled-before", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCancelledAppointmentsBefore(@RequestParam String date){

        String returnedMessage = appointmentService.deleteCancelledAppointmentsBefore(LocalDate.parse(date));

        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

}
