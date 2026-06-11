package com.example.proyectos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "proyectos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto")
    private Long idProyecto;

    private String nombre;
    private String descripcion;

    private String estado;
    private String prioridad;

    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
}