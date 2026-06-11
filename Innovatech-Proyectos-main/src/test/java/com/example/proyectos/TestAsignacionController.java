package com.example.proyectos;

import com.example.proyectos.controller.AsignacionController;
import com.example.proyectos.model.Asignacion;
import com.example.proyectos.model.Tarea;
import com.example.proyectos.service.AsignacionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AsignacionController.class)
class TestAsignacionController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AsignacionService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Asignacion crearAsignacion() {

        Tarea tarea = new Tarea();
        tarea.setIdTarea(1L);

        return Asignacion.builder()
                .idAsignacion(1L)
                .tarea(tarea)
                .idUsuario(10L)
                .fechaAsignacion(LocalDate.of(2026, 1, 1))
                .horasAproximadas(8)
                .build();
    }

    @Test
    void testGetAll() throws Exception {

        when(service.listarAsignaciones())
                .thenReturn(List.of(crearAsignacion()));

        mockMvc.perform(get("/api/proyectos/asignacion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idAsignacion").value(1))
                .andExpect(jsonPath("$[0].idUsuario").value(10));
    }

    @Test
    void testGetById() throws Exception {

        when(service.obtenerAsignacion(1L))
                .thenReturn(crearAsignacion());

        mockMvc.perform(get("/api/proyectos/asignacion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAsignacion").value(1))
                .andExpect(jsonPath("$.idUsuario").value(10));
    }

    @Test
    void testCreate() throws Exception {

        Asignacion asignacion = crearAsignacion();

        when(service.crearAsignacion(any(Asignacion.class)))
                .thenReturn(asignacion);

        mockMvc.perform(post("/api/proyectos/asignacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asignacion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAsignacion").value(1))
                .andExpect(jsonPath("$.idUsuario").value(10));
    }

    @Test
    void testUpdate() throws Exception {

        Asignacion asignacion = crearAsignacion();

        when(service.actualizarAsignacion(any(Long.class), any(Asignacion.class)))
                .thenReturn(asignacion);

        mockMvc.perform(put("/api/proyectos/asignacion/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asignacion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAsignacion").value(1));
    }

    @Test
    void testDelete() throws Exception {

        doNothing().when(service).eliminarAsignacion(1L);

        mockMvc.perform(delete("/api/proyectos/asignacion/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetByUsuario() throws Exception {

        when(service.listarPorUsuario(10L))
                .thenReturn(List.of(crearAsignacion()));

        mockMvc.perform(get("/api/proyectos/usuarios/10/asignaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(10));
    }

    @Test
    void testGetByTarea() throws Exception {

        when(service.listarPorTarea(1L))
                .thenReturn(List.of(crearAsignacion()));

        mockMvc.perform(get("/api/proyectos/tareas/1/asignaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idAsignacion").value(1));
    }

    @Test
    void testGetByHoras() throws Exception {

        when(service.listarPorHoras(8))
                .thenReturn(List.of(crearAsignacion()));

        mockMvc.perform(get("/api/proyectos/asignacion/horas/8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].horasAproximadas").value(8));
    }
}