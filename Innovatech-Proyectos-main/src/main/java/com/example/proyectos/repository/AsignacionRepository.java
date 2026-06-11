package com.example.proyectos.repository;

import com.example.proyectos.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    List<Asignacion> findByIdUsuario(Long idUsuario);

    List<Asignacion> findByTarea_IdTarea(Long idTarea);

    // Por si necesitamos buscar por horas
    List<Asignacion> findByHorasAproximadas(Integer horasAproximadas);
}