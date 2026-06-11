package com.example.proyectos.controller;

import com.example.proyectos.model.Tarea;
import com.example.proyectos.service.TareaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class TareaController {

    private final TareaService service;

    public TareaController(TareaService service) {
        this.service = service;
    }

    @GetMapping("/{proyecto_id}/tareas")
    public List<Tarea> getByProyecto(@PathVariable Long proyecto_id) {
        return service.listarPorProyecto(proyecto_id);
    }

    @GetMapping("/tareas")
    public List<Tarea> getAll() {
        return service.listarTareas();
    }

    @GetMapping("/tareas/{id}")
    public Tarea getById(@PathVariable Long id) {
        return service.obtenerTarea(id);
    }

    @PostMapping("/tareas")
    public Tarea create(@RequestBody Tarea tarea) {
        return service.crearTarea(tarea);
    }

    @PutMapping("/tareas/{id}")
    public Tarea update(@PathVariable Long id, @RequestBody Tarea tarea) {
        return service.actualizarTarea(id, tarea);
    }

    @PatchMapping("/tareas/{id}/estado")
    public Tarea updateEstado(@PathVariable Long id, @RequestBody Tarea tarea) {
        return service.actualizarEstado(id, tarea.getEstado());
    }

    @DeleteMapping("/tareas/{id}")
    public void delete(@PathVariable Long id) {
        service.eliminarTarea(id);
    }
}