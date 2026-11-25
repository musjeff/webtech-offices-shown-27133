package com.mediclinic.hospital.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediclinic.hospital.domain.Doctor;
import com.mediclinic.hospital.service.DoctorService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController 
@RequestMapping (value = "/api/doctors")
public class DoctorController {

@Autowired 
    private DoctorService doctorService;

@PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?>  saveDoctor(@RequestBody  Doctor doctor){

        String returnedMessage = doctorService.saveDoctor(doctor);

        if(returnedMessage.equalsIgnoreCase("Doctor is saved Successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
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
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor){

        String returnedMessage = doctorService.updateDoctor(id, doctor);

        if(returnedMessage.equalsIgnoreCase("Doctor is updated Successfully")){
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
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id){

        String returnedMessage = doctorService.deleteDoctor(id);

        if(returnedMessage.equalsIgnoreCase("Doctor is deleted Successfully")){
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
    public ResponseEntity<?> getDoctorById(@PathVariable UUID id){

        Doctor doctor = doctorService.getDoctorById(id);

        if (doctor == null) {
            return new ResponseEntity<>("No Doctor found with id "+id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getAllDoctors(){

        return new ResponseEntity<>(doctorService.getAllDoctors(), HttpStatus.OK);
    }

@GetMapping(value = "/find/firstName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getDoctorsByFirstName(@RequestParam String firstName){

        return new ResponseEntity<>(doctorService.getDoctorsByFirstName(firstName), HttpStatus.OK);
    }

@GetMapping(value = "/find/lastName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getDoctorsByLastName(@RequestParam String lastName){

        return new ResponseEntity<>(doctorService.getDoctorsByLastName(lastName), HttpStatus.OK);
    }

@GetMapping(value = "/find/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getDoctorsByFullName(@RequestParam String firstName, @RequestParam String lastName){

        return new ResponseEntity<>(doctorService.getDoctorsByFullName(firstName, lastName), HttpStatus.OK);
    }

@GetMapping(value = "/find/specialization/{specializationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(@PathVariable UUID specializationId){

        return new ResponseEntity<>(doctorService.getDoctorsBySpecialization(specializationId), HttpStatus.OK);
    }

@GetMapping(value = "/by-specialization", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getDoctorsBySpecializationName(@RequestParam String name){

        return new ResponseEntity<>(doctorService.getDoctorsBySpecializationName(name), HttpStatus.OK);
    }

@GetMapping(value = "/without-office", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getDoctorsWithoutOffice(){

        return new ResponseEntity<>(doctorService.getDoctorsWithoutOffice(), HttpStatus.OK);
    }

}
