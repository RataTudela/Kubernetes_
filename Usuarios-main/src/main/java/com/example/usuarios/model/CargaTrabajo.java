package com.example.usuarios.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "carga")
@Data
public class CargaTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carga;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false) 
    @JsonBackReference
    private Usuario usuario;    
    private Integer horas_asignadas;
    private String nombreTarea;

    @Column(name = "id_tarea")
    private Long idTarea;
}