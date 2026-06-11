package com.example.proyectos.service;

import com.example.proyectos.model.Asignacion;
import com.example.proyectos.model.Tarea;
import com.example.proyectos.repository.AsignacionRepository;
import com.example.proyectos.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsignacionService {

    private final AsignacionRepository repository;
    private final TareaRepository tareaRepository;

    public List<Asignacion> listarAsignaciones() {
        return repository.findAll();
    }

    public Asignacion obtenerAsignacion(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Asignacion crearAsignacion(Asignacion asignacion) {
        if (asignacion.getTarea() != null) {
            Long id = asignacion.getTarea().getIdTarea();
            Tarea tarea = tareaRepository.findById(id).orElse(null);
            asignacion.setTarea(tarea);
        }

        return repository.save(asignacion);
    }

    public Asignacion actualizarAsignacion(Long id, Asignacion asignacion) {
        Asignacion existente = repository.findById(id).orElse(null);

        if (existente != null) {
            existente.setIdUsuario(asignacion.getIdUsuario());
            existente.setFechaAsignacion(asignacion.getFechaAsignacion());


            existente.setHorasAproximadas(asignacion.getHorasAproximadas());

            if (asignacion.getTarea() != null) {
                Tarea tarea = tareaRepository
                        .findById(asignacion.getTarea().getIdTarea())
                        .orElse(null);
                existente.setTarea(tarea);
            }

            return repository.save(existente);
        }
        return null;
    }

    public void eliminarAsignacion(Long id) {
        repository.deleteById(id);
    }

    public List<Asignacion> listarPorUsuario(Long idUsuario) {
        return repository.findByIdUsuario(idUsuario);
    }

    public List<Asignacion> listarPorTarea(Long idTarea) {
        return repository.findByTarea_IdTarea(idTarea);
    }


    public List<Asignacion> listarPorHoras(Integer horas) {
        return repository.findByHorasAproximadas(horas);
    }
}