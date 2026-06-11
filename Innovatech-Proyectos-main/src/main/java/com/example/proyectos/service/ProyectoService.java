package com.example.proyectos.service;

import com.example.proyectos.model.ResumenProyecto;
import com.example.proyectos.model.Proyecto;
import com.example.proyectos.repository.ProyectoRepository;
import com.example.proyectos.repository.TareaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository repository;
    private final TareaRepository tareaRepository;

    public List<Proyecto> listarProyectos() {
        return repository.findAll();
    }

    public Proyecto obtenerProyectoPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Proyecto crearProyecto(Proyecto proyecto) {
        return repository.save(proyecto);
    }

    public Proyecto actualizarProyecto(Long id, Proyecto proyecto) {
        Proyecto existente = repository.findById(id).orElse(null);

        if (existente != null) {
            existente.setNombre(proyecto.getNombre());
            existente.setDescripcion(proyecto.getDescripcion());
            existente.setEstado(proyecto.getEstado());
            existente.setPrioridad(proyecto.getPrioridad());
            existente.setFecha_inicio(proyecto.getFecha_inicio());
            existente.setFecha_fin(proyecto.getFecha_fin());

            return repository.save(existente);
        }

        return null;
    }

    public Proyecto actualizarEstado(Long id, String estado) {
        Proyecto proyecto = repository.findById(id).orElse(null);

        if (proyecto != null) {
            proyecto.setEstado(estado);
            return repository.save(proyecto);
        }

        return null;
    }

    public void eliminarProyecto(Long id) {
        repository.deleteById(id);
    }

    public List<ResumenProyecto> obtenerResumenTareas() {
        List<Proyecto> proyectos = repository.findAll();

        return proyectos.stream().map(p -> {
            long totales = tareaRepository.countByProyecto_IdProyecto(
                    p.getIdProyecto()
            );

            long completadas = tareaRepository.countByProyecto_IdProyectoAndEstadoIgnoreCase(
                    p.getIdProyecto(),
                    "FINALIZADO"
            );

            return new ResumenProyecto(
                    p.getIdProyecto(),
                    completadas,
                    totales
            );
        }).collect(Collectors.toList());
    }
}