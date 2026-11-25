package com.mediclinic.hospital.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediclinic.hospital.domain.Patient;
import com.mediclinic.hospital.service.PatientService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController 
@RequestMapping (value = "/api/patients")
public class PatientController {

@Autowired 
    private PatientService patientService;

@PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?>  savePatient(@RequestBody  Patient patient){

        String returnedMessage = patientService.savePatient(patient);

        if(returnedMessage.equalsIgnoreCase("Patient is saved Successfully")){
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
    public ResponseEntity<?> updatePatient(@PathVariable UUID id, @RequestBody Patient patient){

        String returnedMessage = patientService.updatePatient(id, patient);

        if(returnedMessage.equalsIgnoreCase("Patient is updated Successfully")){
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
    public ResponseEntity<?> deletePatient(@PathVariable UUID id){

        String returnedMessage = patientService.deletePatient(id);

        if(returnedMessage.equalsIgnoreCase("Patient is deleted Successfully")){
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
    public ResponseEntity<?> getPatientById(@PathVariable UUID id){

        Patient patient = patientService.getPatientById(id);

        if (patient == null) {
            return new ResponseEntity<>("No Patient found with id "+id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Patient>> getAllPatients(){

        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.OK);
    }

@GetMapping(value = "/find/firstName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Patient>> getPatientsByFirstName(@RequestParam String firstName){

        return new ResponseEntity<>(patientService.getPatientsByFirstName(firstName), HttpStatus.OK);
    }

@GetMapping(value = "/by-last-name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Patient>> getPatientsByLastName(@RequestParam String lastName){

        return new ResponseEntity<>(patientService.getPatientsByLastName(lastName), HttpStatus.OK);
    }

@GetMapping(value = "/find/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Patient>> getPatientsByFullName(@RequestParam String firstName, @RequestParam String lastName){

        return new ResponseEntity<>(patientService.getPatientsByFullName(firstName, lastName), HttpStatus.OK);
    }

@GetMapping(value = "/of-doctor/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPatientsOfDoctor(@PathVariable UUID doctorId){

        if(patientService.getDoctorById(doctorId).isEmpty()){
            return new ResponseEntity<>("The doctor with that id does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(patientService.getPatientsOfDoctor(doctorId), HttpStatus.OK);
    }

@GetMapping(value = "/frequent", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Patient>> getFrequentPatients(@RequestParam long min){

        return new ResponseEntity<>(patientService.getFrequentPatients(min), HttpStatus.OK);
    }

}
