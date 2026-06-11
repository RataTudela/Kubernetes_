package com.example.usuarios.controller;

import com.example.usuarios.model.Disponibilidad;
import com.example.usuarios.service.DisponibilidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios/disponibilidad")
public class DisponibilidadController {

    @Autowired
    private DisponibilidadService service;

    @GetMapping
    public List<Disponibilidad> listarTodo() {
        return service.listarTodas();
    }

    @GetMapping("/{usuarioId}")
    public List<Disponibilidad> buscarPorUsuario(@PathVariable Long usuarioId) {
        return service.buscarPorUsuario(usuarioId);
    }

    @PostMapping
    public Disponibilidad crear(@RequestBody Disponibilidad d) {
        return service.guardar(d);
    }

    @PutMapping("/{id}")
    public Disponibilidad modificar(@PathVariable Long id, @RequestBody Disponibilidad d) {
    Disponibilidad existente = service.buscarPorId(id);
    if (existente != null) {
        existente.setFechaInicio(d.getFechaInicio());
        existente.setFechaFin(d.getFechaFin());
        existente.setMotivo(d.getMotivo());
        
        if (d.getUsuario() != null) {
            existente.setUsuario(d.getUsuario());
        }
        
        return service.guardar(existente);
    }
    return null;
}

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/buscar")
    public List<Disponibilidad> consultarReal(@RequestParam String motivo) {
        return service.buscarReal(motivo);
    }
}
