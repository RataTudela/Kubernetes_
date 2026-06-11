package com.example.proyectos.repository;

import com.example.proyectos.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    // Recupera la lista de tareas asociadas a un proyecto (Mantenido del original)
    List<Tarea> findByProyecto_IdProyecto(Long idProyecto);

    // Cuenta el total de tareas de un proyecto (Mantenido del original)
    long countByProyecto_IdProyecto(Long idProyecto);

    // CORRECCIÓN DEFINITIVA: Cuenta por estado ignorando si es mayúscula o minúscula
    long countByProyecto_IdProyectoAndEstadoIgnoreCase(Long idProyecto, String estado);
}