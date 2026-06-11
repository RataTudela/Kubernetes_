package com.example.usuarios.service;

import com.example.usuarios.model.Disponibilidad;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.DisponibilidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DisponibilidadService {
    @Autowired
    private DisponibilidadRepository repository;

    public List<Disponibilidad> listarTodas() {
         return repository.findAll(); 
        }

    public Disponibilidad guardar(Disponibilidad d) {
         return repository.save(d); 
        }

    public List<Disponibilidad> buscarPorUsuario(Long id) {
    return repository.buscarPorUsuarioId(id); 
    }

    public Disponibilidad buscarPorId(Long id) {
         return repository.findById(id).orElse(null); 
        }

    @Transactional
    public void eliminar(Long id) {
    Disponibilidad disponibilidad = repository.findById(id).orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada con ID: " + id));
    Usuario usuario = disponibilidad.getUsuario();
    if (usuario != null) {
        usuario.getDisponibilidades().remove(disponibilidad);
    }
    repository.delete(disponibilidad);
    }
    
    public List<Disponibilidad> buscarReal(String motivo) {
        return repository.findByMotivoContaining(motivo);
    }
}