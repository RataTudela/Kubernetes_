package com.example.proyectos;

import com.example.proyectos.controller.TareaController;
import com.example.proyectos.model.Proyecto;
import com.example.proyectos.model.Tarea;
import com.example.proyectos.service.TareaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TareaController.class)
class TestTareaController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TareaService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Tarea crearTarea() {

        Proyecto proyecto = new Proyecto();
        proyecto.setIdProyecto(1L);

        Tarea tarea = new Tarea();
        tarea.setIdTarea(1L);
        tarea.setNombre("Tarea Test");
        tarea.setDescripcion("Descripcion Test");
        tarea.setEstado("PENDIENTE");
        tarea.setProyecto(proyecto);

        return tarea;
    }

    @Test
    void testGetAll() throws Exception {

        when(service.listarTareas())
                .thenReturn(List.of(crearTarea()));

        mockMvc.perform(get("/api/proyectos/tareas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTarea").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Tarea Test"));
    }

    @Test
    void testGetByProyecto() throws Exception {

        when(service.listarPorProyecto(1L))
                .thenReturn(List.of(crearTarea()));

        mockMvc.perform(get("/api/proyectos/1/tareas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTarea").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Tarea Test"));
    }

    @Test
    void testGetById() throws Exception {

        when(service.obtenerTarea(1L))
                .thenReturn(crearTarea());

        mockMvc.perform(get("/api/proyectos/tareas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTarea").value(1))
                .andExpect(jsonPath("$.nombre").value("Tarea Test"));
    }

    @Test
    void testCreate() throws Exception {

        Tarea tarea = crearTarea();

        when(service.crearTarea(any(Tarea.class)))
                .thenReturn(tarea);

        mockMvc.perform(post("/api/proyectos/tareas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarea)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTarea").value(1))
                .andExpect(jsonPath("$.nombre").value("Tarea Test"));
    }

    @Test
    void testUpdate() throws Exception {

        Tarea tarea = crearTarea();

        when(service.actualizarTarea(anyLong(), any(Tarea.class)))
                .thenReturn(tarea);

        mockMvc.perform(put("/api/proyectos/tareas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarea)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTarea").value(1))
                .andExpect(jsonPath("$.nombre").value("Tarea Test"));
    }

    @Test
    void testUpdateEstado() throws Exception {

        Tarea tarea = crearTarea();
        tarea.setEstado("FINALIZADA");

        when(service.actualizarEstado(1L, "FINALIZADA"))
                .thenReturn(tarea);

        mockMvc.perform(patch("/api/proyectos/tareas/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarea)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("FINALIZADA"));
    }

    @Test
    void testDelete() throws Exception {

        doNothing().when(service).eliminarTarea(1L);

        mockMvc.perform(delete("/api/proyectos/tareas/1"))
                .andExpect(status().isOk());
    }
}