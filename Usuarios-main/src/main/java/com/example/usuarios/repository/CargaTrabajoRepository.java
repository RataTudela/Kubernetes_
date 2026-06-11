package com.example.usuarios.repository;

import com.example.usuarios.model.CargaTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CargaTrabajoRepository extends JpaRepository<CargaTrabajo, Long> {
    
    @Query("SELECT c FROM CargaTrabajo c WHERE c.usuario.id_usuario = :id")
    List<CargaTrabajo> findByUsuarioId(@Param("id") Long id);

    @Query("SELECT c FROM CargaTrabajo c WHERE c.usuario.id_usuario = :id AND c.nombreTarea = :nombreTarea")
    CargaTrabajo findByUsuarioIdAndNombreTarea(@Param("id") Long id, @Param("nombreTarea") String nombreTarea);

    List<CargaTrabajo> findByNombreTarea(String nombreTarea);
}