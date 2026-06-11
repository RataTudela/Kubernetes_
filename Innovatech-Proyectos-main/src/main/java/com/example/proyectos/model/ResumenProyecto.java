package com.example.proyectos.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Permite a Jackson serializar/deserializar el JSON sin problemas
public class ResumenProyecto {

    private Long id_proyecto;
    private Long tareas_completadas;
    private Long tareas_totales;

    // Constructor explícito requerido para el mapeo del stream en ProyectoService
    public ResumenProyecto(Long id_proyecto, Long tareas_completadas, Long tareas_totales) {
        this.id_proyecto = id_proyecto;
        this.tareas_completadas = tareas_completadas;
        this.tareas_totales = tareas_totales;
    }
}