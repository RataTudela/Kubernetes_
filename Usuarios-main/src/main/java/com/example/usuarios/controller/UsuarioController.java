package com.example.usuarios.controller;

import com.example.usuarios.model.Usuario;
import com.example.usuarios.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.guardar(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario detalle) {
        return usuarioService.obtenerPorId(id).map(usuario -> {
            usuario.setNombre(detalle.getNombre());
            usuario.setEmail(detalle.getEmail());
            usuario.setContraseña(detalle.getContraseña());
            usuario.setRol(detalle.getRol());
            usuario.setEstado(detalle.getEstado());
            return ResponseEntity.ok(usuarioService.guardar(usuario));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/rol")
    public ResponseEntity<Usuario> modificarRol(@PathVariable Long id, @RequestBody String nuevoRol) {
        return usuarioService.obtenerPorId(id).map(usuario -> {
            usuario.setRol(nuevoRol);
            return ResponseEntity.ok(usuarioService.guardar(usuario));
        }).orElse(ResponseEntity.notFound().build());
    }
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Usuario> modificarEstado(@PathVariable Long id, @RequestBody String nuevoEstado) {
        return usuarioService.obtenerPorId(id).map(usuario -> {
            usuario.setEstado(nuevoEstado);
            return ResponseEntity.ok(usuarioService.guardar(usuario));
        }).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario credenciales) {
    Optional<Usuario> usuarioOpt = usuarioService.autenticar(credenciales.getEmail(), credenciales.getContraseña());

    if (usuarioOpt.isPresent()) {
        return ResponseEntity.ok(usuarioOpt.get()); 
    } else {
        return ResponseEntity.status(401).body("Email o contraseña incorrectos"); 
    }
}
}