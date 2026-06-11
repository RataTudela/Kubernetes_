package com.example.proyectos;

import com.example.proyectos.model.Asignacion;
import com.example.proyectos.model.Tarea;
import com.example.proyectos.repository.AsignacionRepository;
import com.example.proyectos.repository.TareaRepository;
import com.example.proyectos.service.AsignacionService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestAsignacionService {

    @Mock
    private AsignacionRepository repository;

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private AsignacionService service;

    private Asignacion asignacion;
    private Tarea tarea;

    @BeforeEach
    void setUp() {
        tarea = Tarea.builder()
                .idTarea(1L)
                .nombre("Tarea Test")
                .build();

        asignacion = Asignacion.builder()
                .idAsignacion(1L)
                .idUsuario(10L)
                .fechaAsignacion(LocalDate.now())
                .horasAproximadas(5)
                .tarea(tarea)
                .build();
    }

    @Test
    void listarAsignaciones() {
        when(repository.findAll()).thenReturn(List.of(asignacion));

        List<Asignacion> resultado = service.listarAsignaciones();

        assertEquals(1, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void obtenerAsignacion() {
        when(repository.findById(1L)).thenReturn(Optional.of(asignacion));

        Asignacion resultado = service.obtenerAsignacion(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdAsignacion());
    }

    @Test
    void crearAsignacion() {
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));
        when(repository.save(asignacion)).thenReturn(asignacion);

        Asignacion resultado = service.crearAsignacion(asignacion);

        assertNotNull(resultado);
        verify(repository).save(asignacion);
    }

    @Test
    void actualizarAsignacion() {
        Asignacion nueva = Asignacion.builder()
                .idUsuario(20L)
                .fechaAsignacion(LocalDate.now())
                .horasAproximadas(8)
                .tarea(tarea)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(asignacion));
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));
        when(repository.save(any(Asignacion.class))).thenReturn(asignacion);

        Asignacion resultado = service.actualizarAsignacion(1L, nueva);

        assertNotNull(resultado);
        verify(repository).save(any(Asignacion.class));
    }

    @Test
    void eliminarAsignacion() {
        doNothing().when(repository).deleteById(1L);

        service.eliminarAsignacion(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void listarPorUsuario() {
        when(repository.findByIdUsuario(10L)).thenReturn(List.of(asignacion));

        List<Asignacion> resultado = service.listarPorUsuario(10L);

        assertEquals(1, resultado.size());
    }

    @Test
    void listarPorTarea() {
        when(repository.findByTarea_IdTarea(1L)).thenReturn(List.of(asignacion));

        List<Asignacion> resultado = service.listarPorTarea(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void listarPorHoras() {
        when(repository.findByHorasAproximadas(5)).thenReturn(List.of(asignacion));

        List<Asignacion> resultado = service.listarPorHoras(5);

        assertEquals(1, resultado.size());
    }
}