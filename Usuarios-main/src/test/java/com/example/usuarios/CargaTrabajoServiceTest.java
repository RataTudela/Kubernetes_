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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.usuarios.model.CargaTrabajo;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.CargaTrabajoRepository;
import com.example.usuarios.service.CargaTrabajoService;

@ExtendWith(MockitoExtension.class)
public class CargaTrabajoServiceTest {

    @Mock
    private CargaTrabajoRepository repository;

    @InjectMocks
    private CargaTrabajoService service;

    private CargaTrabajo cargaPrueba;

    private Usuario usuarioPrueba;

    @BeforeEach
    void setUp() {
        usuarioPrueba = new Usuario();
        usuarioPrueba.setId_usuario(1L);
        usuarioPrueba.setCargasTrabajo(new ArrayList<>()); // Inicializamos la lista

        cargaPrueba = new CargaTrabajo();
        cargaPrueba.setId_carga(1L); 
        cargaPrueba.setHoras_asignadas(20);
        cargaPrueba.setNombreTarea("Desarrollo Backend");
        cargaPrueba.setUsuario(usuarioPrueba); // Asociamos el usuario a la carga
    }

    @Test
    void testSumarHoras_Exito() {
        Mockito.when(repository.findByUsuarioIdAndNombreTarea(1L, "Desarrollo Backend"))
               .thenReturn(cargaPrueba);
        
        Mockito.when(repository.save(any(CargaTrabajo.class)))
               .thenAnswer(i -> i.getArguments()[0]);

        CargaTrabajo resultado = service.sumarHoras(1L, "Desarrollo Backend", 10);

        assertNotNull(resultado);
        assertEquals(30, resultado.getHoras_asignadas());
        Mockito.verify(repository).save(cargaPrueba);
    }

    @Test
    void testSumarHoras_NoExiste() {
        Mockito.when(repository.findByUsuarioIdAndNombreTarea(1L, "Tarea Inexistente"))
               .thenReturn(null);

        CargaTrabajo resultado = service.sumarHoras(1L, "Tarea Inexistente", 10);

        assertNull(resultado);
        Mockito.verify(repository, Mockito.never()).save(any());
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());
        CargaTrabajo resultado = service.buscarPorId(99L);
        assertNull(resultado);
    }
    @Test
    void testListarTodas() {
        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(cargaPrueba));
        List<CargaTrabajo> resultado = service.listarTodas();
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void testGuardar() {
        Mockito.when(repository.save(any(CargaTrabajo.class))).thenReturn(cargaPrueba);
        CargaTrabajo guardada = service.guardar(new CargaTrabajo());
        assertNotNull(guardada);
        Mockito.verify(repository).save(any(CargaTrabajo.class));
    }

    @Test
    void testBuscarPorUsuario() {
        Mockito.when(repository.findByUsuarioId(1L)).thenReturn(Arrays.asList(cargaPrueba));
        List<CargaTrabajo> resultado = service.buscarPorUsuario(1L);
        assertEquals(1, resultado.size());
        Mockito.verify(repository).findByUsuarioId(1L);
    }

    @Test
    void testEliminar_Exito() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(cargaPrueba));
        service.eliminar(1L);

        Mockito.verify(repository, Mockito.times(1)).delete(cargaPrueba);
        assertFalse(usuarioPrueba.getCargasTrabajo().contains(cargaPrueba));
    }

    @Test
    void testEliminar_NoEncontrado() {
        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            service.eliminar(99L);
        });
    }

    @Test
    void testBuscarPorNombreTarea() {
        Mockito.when(repository.findByNombreTarea("Desarrollo Backend")).thenReturn(Arrays.asList(cargaPrueba));
        List<CargaTrabajo> resultado = service.buscarPorNombreTarea("Desarrollo Backend");
        assertFalse(resultado.isEmpty());
        Mockito.verify(repository).findByNombreTarea("Desarrollo Backend");
    }
}