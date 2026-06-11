package com.example.proyectos.service;

import com.example.proyectos.model.Proyecto;
import com.example.proyectos.model.Tarea;
import com.example.proyectos.repository.ProyectoRepository;
import com.example.proyectos.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository repository;
    private final ProyectoRepository proyectoRepository;

    public List<Tarea> listarTareas() {
        return repository.findAll();
    }

    public List<Tarea> listarPorProyecto(Long proyectoId) {
        return repository.findByProyecto_IdProyecto(proyectoId);
    }

    public Tarea obtenerTarea(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Tarea crearTarea(Tarea tarea) {
        if (tarea.getProyecto() != null) {
            Long id = tarea.getProyecto().getIdProyecto();
            Proyecto proyecto = proyectoRepository.findById(id).orElse(null);
            tarea.setProyecto(proyecto);
        }
        return repository.save(tarea);
    }

    public Tarea actualizarTarea(Long id, Tarea tarea) {
        Tarea existente = repository.findById(id).orElse(null);

        if (existente != null) {
            existente.setNombre(tarea.getNombre());
            existente.setDescripcion(tarea.getDescripcion());
            existente.setEstado(tarea.getEstado());

            if (tarea.getProyecto() != null) {
                Proyecto proyecto = proyectoRepository
                        .findById(tarea.getProyecto().getIdProyecto())
                        .orElse(null);
                existente.setProyecto(proyecto);
            }

            return repository.save(existente);
        }
        return null;
    }

    public Tarea actualizarEstado(Long id, String estado) {
        Tarea tarea = repository.findById(id).orElse(null);

        if (tarea != null) {
            tarea.setEstado(estado);
            return repository.save(tarea);
        }
        return null;
    }

    public void eliminarTarea(Long id) {
        repository.deleteById(id);
    }
}