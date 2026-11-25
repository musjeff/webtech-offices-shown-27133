package com.mediclinic.hospital.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity 
public class Specialization {

    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
     private UUID id;

     @Column (name="name")
     private String name;

     public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     @ManyToMany 
     @JoinTable (
        name = "doctor_specialization",
        joinColumns = @JoinColumn (name="doctor_id"),
        inverseJoinColumns = @JoinColumn (name="specialization_id")
     )
     private List<Doctor> doctors = new ArrayList<>();

     public List<Doctor> getDoctors() {
         return doctors;
     }

}
