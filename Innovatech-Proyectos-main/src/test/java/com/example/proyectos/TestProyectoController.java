package com.example.proyectos;

import com.example.proyectos.controller.ProyectoController;
import com.example.proyectos.model.Proyecto;
import com.example.proyectos.model.ResumenProyecto;
import com.example.proyectos.service.ProyectoService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProyectoController.class)
class TestProyectoController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProyectoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Proyecto crearProyecto() {

        return Proyecto.builder()
                .idProyecto(1L)
                .nombre("Proyecto Test")
                .descripcion("Descripcion Test")
                .estado("EN_PROCESO")
                .prioridad("ALTA")
                .fecha_inicio(LocalDate.of(2026, 1, 1))
                .fecha_fin(LocalDate.of(2026, 12, 31))
                .build();
    }

    @Test
    void testGetAll() throws Exception {

        when(service.listarProyectos())
                .thenReturn(List.of(crearProyecto()));

        mockMvc.perform(get("/api/proyectos/proyecto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProyecto").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Proyecto Test"));
    }

    @Test
    void testGetById() throws Exception {

        when(service.obtenerProyectoPorId(1L))
                .thenReturn(crearProyecto());

        mockMvc.perform(get("/api/proyectos/proyecto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProyecto").value(1))
                .andExpect(jsonPath("$.nombre").value("Proyecto Test"));
    }

    @Test
    void testCreate() throws Exception {

        Proyecto proyecto = crearProyecto();

        when(service.crearProyecto(any(Proyecto.class)))
                .thenReturn(proyecto);

        mockMvc.perform(post("/api/proyectos/proyecto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proyecto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProyecto").value(1))
                .andExpect(jsonPath("$.nombre").value("Proyecto Test"));
    }

    @Test
    void testUpdate() throws Exception {

        Proyecto proyecto = crearProyecto();

        when(service.actualizarProyecto(anyLong(), any(Proyecto.class)))
                .thenReturn(proyecto);

        mockMvc.perform(put("/api/proyectos/proyecto/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proyecto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProyecto").value(1))
                .andExpect(jsonPath("$.nombre").value("Proyecto Test"));
    }

    @Test
    void testUpdateEstado() throws Exception {

        Proyecto proyecto = crearProyecto();
        proyecto.setEstado("FINALIZADO");

        when(service.actualizarEstado(1L, "FINALIZADO"))
                .thenReturn(proyecto);

        mockMvc.perform(patch("/api/proyectos/proyecto/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proyecto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("FINALIZADO"));
    }

    @Test
    void testDelete() throws Exception {

        doNothing().when(service).eliminarProyecto(1L);

        mockMvc.perform(delete("/api/proyectos/proyecto/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetResumen() throws Exception {

        ResumenProyecto resumen =
                new ResumenProyecto(
                        1L,
                        7L,
                        10L
                );

        when(service.obtenerResumenTareas())
                .thenReturn(List.of(resumen));

        mockMvc.perform(get("/api/proyectos/proyecto/resumen-tareas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_proyecto").value(1))
                .andExpect(jsonPath("$[0].tareas_completadas").value(7))
                .andExpect(jsonPath("$[0].tareas_totales").value(10));
    }
}