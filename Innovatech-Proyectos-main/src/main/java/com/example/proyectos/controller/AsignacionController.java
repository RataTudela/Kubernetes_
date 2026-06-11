package com.example.proyectos.controller;

import com.example.proyectos.model.Asignacion;
import com.example.proyectos.service.AsignacionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class AsignacionController {

    private final AsignacionService service;

    public AsignacionController(AsignacionService service) {
        this.service = service;
    }

    @GetMapping("/asignacion")
    public List<Asignacion> getAll() {
        return service.listarAsignaciones();
    }

    @GetMapping("/asignacion/{id}")
    public Asignacion getById(@PathVariable Long id) {
        return service.obtenerAsignacion(id);
    }

    @PostMapping("/asignacion")
    public Asignacion create(@RequestBody Asignacion asignacion) {
        return service.crearAsignacion(asignacion);
    }

    @PutMapping("/asignacion/{id}")
    public Asignacion update(@PathVariable Long id, @RequestBody Asignacion asignacion) {
        return service.actualizarAsignacion(id, asignacion);
    }

    @DeleteMapping("/asignacion/{id}")
    public void delete(@PathVariable Long id) {
        service.eliminarAsignacion(id);
    }

    @GetMapping("/usuarios/{id_usuario}/asignaciones")
    public List<Asignacion> getByUsuario(@PathVariable Long id_usuario) {
        return service.listarPorUsuario(id_usuario);
    }

    @GetMapping("/tareas/{id_tarea}/asignaciones")
    public List<Asignacion> getByTarea(@PathVariable Long id_tarea) {
        return service.listarPorTarea(id_tarea);
    }

    // NUEVO (opcional)
    @GetMapping("/asignacion/horas/{horas}")
    public List<Asignacion> getByHoras(@PathVariable Integer horas) {
        return service.listarPorHoras(horas);
    }
}