package com.example.usuarios.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.usuarios.config.RabbitMqConfig;
import com.example.usuarios.model.CargaTrabajo;
import com.example.usuarios.service.CargaTrabajoService;

@RestController
@RequestMapping("/api/usuarios/carga_trabajo")
public class CargaTrabajoController {

    @Autowired
    private CargaTrabajoService service;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping
    public List<CargaTrabajo> listarTodo() {
        return service.listarTodas();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CargaTrabajo>> buscarPorUsuario(@PathVariable Long usuarioId) {
        List<CargaTrabajo> lista = service.buscarPorUsuario(usuarioId);
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public CargaTrabajo crear(@RequestBody CargaTrabajo c) {
        return service.guardar(c);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CargaTrabajo> modificar(@PathVariable Long id, @RequestBody CargaTrabajo c) {
        CargaTrabajo existente = service.buscarPorId(id);
        if (existente != null) {
            existente.setHoras_asignadas(c.getHoras_asignadas());
            existente.setNombreTarea(c.getNombreTarea());        
            
            if (c.getUsuario() != null) {
                existente.setUsuario(c.getUsuario());
            }
            return ResponseEntity.ok(service.guardar(existente));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/validar/{usuarioId}")
    public ResponseEntity<Boolean> validarCapacidad(
            @PathVariable Long usuarioId, 
            @RequestParam Integer nuevasHoras) {
        List<CargaTrabajo> cargas = service.buscarPorUsuario(usuarioId);
        int totalActual = cargas.stream().mapToInt(CargaTrabajo::getHoras_asignadas).sum();
        boolean esValido = (totalActual + nuevasHoras) <= 40;
        return ResponseEntity.ok(esValido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<CargaTrabajo> consultarPorTarea(@RequestParam String nombreTarea) {
        return service.buscarPorNombreTarea(nombreTarea);
    }

  @PatchMapping("/finalizar-tarea/{idTarea}/carga/{idCarga}")
public ResponseEntity<?> finalizarTarea(@PathVariable Long idTarea, @PathVariable Long idCarga) {
    Map<String, Object> mensaje = new HashMap<>();
    mensaje.put("idTarea", idTarea);
    mensaje.put("estado", "FINALIZADO"); 

    rabbitTemplate.convertAndSend(
        RabbitMqConfig.EXCHANGE_USUARIOS, 
        RabbitMqConfig.ROUTING_KEY_USUARIOS, 
        mensaje
    );

    service.eliminar(idCarga); 

    return ResponseEntity.ok().body("Tarea finalizada en Proyectos y carga liberada en Usuarios.");
}
}