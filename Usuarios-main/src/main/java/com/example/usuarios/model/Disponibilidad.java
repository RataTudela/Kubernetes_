package com.example.usuarios.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "disponibilidades")
@Data
public class Disponibilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_disponibilidad;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false) 
    @JsonBackReference
    private Usuario usuario;    
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String motivo;
}