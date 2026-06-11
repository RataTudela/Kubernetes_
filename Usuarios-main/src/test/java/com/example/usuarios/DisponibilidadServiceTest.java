package com.example.usuarios;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.usuarios.model.Disponibilidad;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.DisponibilidadRepository;
import com.example.usuarios.service.DisponibilidadService;

@ExtendWith(MockitoExtension.class)
public class DisponibilidadServiceTest {

    @Mock
    private DisponibilidadRepository repository;

    @InjectMocks
    private DisponibilidadService service;

    private Disponibilidad disp;

    private Usuario usuarioPrueba;

  @BeforeEach
    void setUp() {
        usuarioPrueba = new Usuario();
        usuarioPrueba.setId_usuario(1L);
        usuarioPrueba.setDisponibilidades(new ArrayList<>()); 

        disp = new Disponibilidad();
        disp.setId_disponibilidad(1L);
        disp.setMotivo("Permiso Médico");
        disp.setUsuario(usuarioPrueba); 
    }

    @Test
    void testListarTodas() {
        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(disp));
        
        List<Disponibilidad> resultado = service.listarTodas();
        
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void testBuscarPorId_Encontrado() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(disp));

        Disponibilidad resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Permiso Médico", resultado.getMotivo());
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());

        Disponibilidad resultado = service.buscarPorId(99L);

        assertNull(resultado);
    }

    @Test
    void testGuardar() {
        Mockito.when(repository.save(any(Disponibilidad.class))).thenReturn(disp);

        Disponibilidad guardada = service.guardar(new Disponibilidad());

        assertNotNull(guardada);
        Mockito.verify(repository).save(any(Disponibilidad.class));
    }

    @Test
    void testBuscarReal() {
        Mockito.when(repository.findByMotivoContaining("Médico")).thenReturn(Arrays.asList(disp));

        List<Disponibilidad> resultado = service.buscarReal("Médico");

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getMotivo().contains("Médico"));
    }
    @Test
    void testEliminar_Exito() {
        // Configuramos: El repo encuentra la disponibilidad
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(disp));
        
        // Ejecutamos
        service.eliminar(1L);

        // Verificamos que se llamó al delete del repositorio
        Mockito.verify(repository, Mockito.times(1)).delete(disp);
        // Verificamos que se intentó remover de la lista del usuario (lógica bidireccional)
        assertFalse(usuarioPrueba.getDisponibilidades().contains(disp));
    }

    @Test
    void testEliminar_NoEncontrado() {
        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.eliminar(99L);
        });

        assertTrue(exception.getMessage().contains("Disponibilidad no encontrada"));
    }

    @Test
    void testBuscarPorUsuario() {
        Mockito.when(repository.buscarPorUsuarioId(1L)).thenReturn(Arrays.asList(disp));

        List<Disponibilidad> resultado = service.buscarPorUsuario(1L);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        Mockito.verify(repository).buscarPorUsuarioId(1L);
    }
}
