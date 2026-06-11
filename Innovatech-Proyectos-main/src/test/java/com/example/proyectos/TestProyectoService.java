package com.example.proyectos;

import com.example.proyectos.model.Proyecto;
import com.example.proyectos.model.ResumenProyecto;
import com.example.proyectos.repository.ProyectoRepository;
import com.example.proyectos.repository.TareaRepository;
import com.example.proyectos.service.ProyectoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestProyectoService {

    @Mock
    private ProyectoRepository repository;

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private ProyectoService service;

    private Proyecto proyecto;

    @BeforeEach
    void setUp() {

        proyecto = Proyecto.builder()
                .idProyecto(1L)
                .nombre("Proyecto Test")
                .descripcion("Descripción")
                .estado("Activo")
                .prioridad("Alta")
                .fecha_inicio(LocalDate.now())
                .fecha_fin(LocalDate.now().plusDays(10))
                .build();
    }

    @Test
    void listarProyectos() {

        when(repository.findAll()).thenReturn(List.of(proyecto));

        List<Proyecto> resultado = service.listarProyectos();

        assertEquals(1, resultado.size());
        assertEquals("Proyecto Test", resultado.get(0).getNombre());
    }

    @Test
    void obtenerProyectoPorId() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(proyecto));

        Proyecto resultado = service.obtenerProyectoPorId(1L);

        assertNotNull(resultado);
        assertEquals("Proyecto Test", resultado.getNombre());
    }

    @Test
    void crearProyecto() {

        when(repository.save(proyecto))
                .thenReturn(proyecto);

        Proyecto resultado = service.crearProyecto(proyecto);

        assertNotNull(resultado);

        verify(repository).save(proyecto);
    }

    @Test
    void actualizarProyecto() {

        Proyecto nuevo = Proyecto.builder()
                .nombre("Nuevo")
                .descripcion("Nueva")
                .estado("Finalizado")
                .prioridad("Media")
                .fecha_inicio(LocalDate.now())
                .fecha_fin(LocalDate.now())
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(proyecto));

        when(repository.save(any(Proyecto.class)))
                .thenReturn(proyecto);

        Proyecto resultado =
                service.actualizarProyecto(1L, nuevo);

        assertNotNull(resultado);

        verify(repository).save(any(Proyecto.class));
    }

    @Test
    void actualizarEstado() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(proyecto));

        when(repository.save(any(Proyecto.class)))
                .thenReturn(proyecto);

        Proyecto resultado =
                service.actualizarEstado(1L, "COMPLETADO");

        assertNotNull(resultado);

        verify(repository).save(any(Proyecto.class));
    }

    @Test
    void eliminarProyecto() {

        doNothing().when(repository).deleteById(1L);

        service.eliminarProyecto(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void obtenerResumenTareas() {

        when(repository.findAll())
                .thenReturn(List.of(proyecto));

        when(tareaRepository.countByProyecto_IdProyecto(1L))
                .thenReturn(10L);

        when(tareaRepository.countByProyecto_IdProyectoAndEstadoIgnoreCase(
                1L,
                "FINALIZADO"
        )).thenReturn(7L);

        List<ResumenProyecto> resultado =
                service.obtenerResumenTareas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        ResumenProyecto resumen = resultado.get(0);

        assertEquals(1L, resumen.getId_proyecto());
        assertEquals(7L, resumen.getTareas_completadas());
        assertEquals(10L, resumen.getTareas_totales());

        verify(repository).findAll();

        verify(tareaRepository)
                .countByProyecto_IdProyecto(1L);

        verify(tareaRepository)
                .countByProyecto_IdProyectoAndEstadoIgnoreCase(
                        1L,
                        "FINALIZADO"
                );
    }
}