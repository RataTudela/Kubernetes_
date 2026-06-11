package com.example.proyectos;

import com.example.proyectos.model.Proyecto;
import com.example.proyectos.model.Tarea;
import com.example.proyectos.repository.ProyectoRepository;
import com.example.proyectos.repository.TareaRepository;
import com.example.proyectos.service.TareaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestTareaService {

    @Mock
    private TareaRepository repository;

    @Mock
    private ProyectoRepository proyectoRepository;

    @InjectMocks
    private TareaService service;

    private Tarea tarea;
    private Proyecto proyecto;

    @BeforeEach
    void setUp() {
        proyecto = Proyecto.builder()
                .idProyecto(1L)
                .nombre("Proyecto")
                .build();

        tarea = Tarea.builder()
                .idTarea(1L)
                .nombre("Tarea")
                .descripcion("Descripción")
                .estado("Pendiente")
                .proyecto(proyecto)
                .build();
    }

    @Test
    void listarTareas() {
        when(repository.findAll()).thenReturn(List.of(tarea));

        List<Tarea> resultado = service.listarTareas();

        assertEquals(1, resultado.size());
    }

    @Test
    void listarPorProyecto() {
        when(repository.findByProyecto_IdProyecto(1L))
                .thenReturn(List.of(tarea));

        List<Tarea> resultado = service.listarPorProyecto(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerTarea() {
        when(repository.findById(1L)).thenReturn(Optional.of(tarea));

        Tarea resultado = service.obtenerTarea(1L);

        assertNotNull(resultado);
        assertEquals("Tarea", resultado.getNombre());
    }

    @Test
    void crearTarea() {
        when(proyectoRepository.findById(1L))
                .thenReturn(Optional.of(proyecto));

        when(repository.save(tarea)).thenReturn(tarea);

        Tarea resultado = service.crearTarea(tarea);

        assertNotNull(resultado);
        verify(repository).save(tarea);
    }

    @Test
    void actualizarTarea() {
        Tarea nueva = Tarea.builder()
                .nombre("Nueva")
                .descripcion("Nueva Desc")
                .estado("Completada")
                .proyecto(proyecto)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(tarea));
        when(proyectoRepository.findById(1L))
                .thenReturn(Optional.of(proyecto));

        when(repository.save(any(Tarea.class))).thenReturn(tarea);

        Tarea resultado = service.actualizarTarea(1L, nueva);

        assertNotNull(resultado);
        verify(repository).save(any(Tarea.class));
    }

    @Test
    void actualizarEstado() {
        when(repository.findById(1L)).thenReturn(Optional.of(tarea));
        when(repository.save(any(Tarea.class))).thenReturn(tarea);

        Tarea resultado = service.actualizarEstado(1L, "Finalizada");

        assertNotNull(resultado);
        verify(repository).save(any(Tarea.class));
    }

    @Test
    void eliminarTarea() {
        doNothing().when(repository).deleteById(1L);

        service.eliminarTarea(1L);

        verify(repository).deleteById(1L);
    }
}