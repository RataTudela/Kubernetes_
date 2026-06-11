package com.example.usuarios.repository;

import com.example.usuarios.model.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {

    @Query("SELECT d FROM Disponibilidad d WHERE d.usuario.id_usuario = :id")
    List<Disponibilidad> buscarPorUsuarioId(@Param("id") Long id);
    
    List<Disponibilidad> findByMotivoContaining(String motivo);
}