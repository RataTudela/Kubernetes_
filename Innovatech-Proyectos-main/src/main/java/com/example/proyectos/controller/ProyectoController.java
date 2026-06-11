package com.example.proyectos.controller;

import com.example.proyectos.model.Proyecto;
import com.example.proyectos.model.ResumenProyecto;
import com.example.proyectos.service.ProyectoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos/proyecto")
public class ProyectoController {

    private final ProyectoService service;

    public ProyectoController(ProyectoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Proyecto> getAll() {
        return service.listarProyectos();
    }

    @GetMapping("/{id}")
    public Proyecto getById(@PathVariable Long id) {
        return service.obtenerProyectoPorId(id);
    }

    @PostMapping
    public Proyecto create(@RequestBody Proyecto proyecto) {
        return service.crearProyecto(proyecto);
    }

    @PutMapping("/{id}")
    public Proyecto update(@PathVariable Long id, @RequestBody Proyecto proyecto) {
        return service.actualizarProyecto(id, proyecto);
    }

    @PatchMapping("/{id}/estado")
    public Proyecto updateEstado(@PathVariable Long id, @RequestBody Proyecto proyecto) {
        return service.actualizarEstado(id, proyecto.getEstado());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.eliminarProyecto(id);
    }
    
    @GetMapping("/resumen-tareas")

    public List<ResumenProyecto> getResumen() {
        return service.obtenerResumenTareas();
    }
}