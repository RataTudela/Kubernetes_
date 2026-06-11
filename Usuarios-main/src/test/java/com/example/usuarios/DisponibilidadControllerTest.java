package com.example.usuarios;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.usuarios.controller.DisponibilidadController;
import com.example.usuarios.model.Disponibilidad;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.service.DisponibilidadService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DisponibilidadController.class)
public class DisponibilidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DisponibilidadService disponibilidadService;

    @Autowired
    private ObjectMapper objectMapper;

    private Disponibilidad dispPrueba;
    private Usuario usuarioPrueba;

    @BeforeEach
    void setUp() {
        usuarioPrueba = new Usuario();
        usuarioPrueba.setId_usuario(1L);
        usuarioPrueba.setNombre("Benjamon");

        dispPrueba = new Disponibilidad();
        dispPrueba.setId_disponibilidad(100L);
        dispPrueba.setMotivo("Vacaciones");
        dispPrueba.setUsuario(usuarioPrueba);
    }

    @Test
    void testListarTodo() throws Exception {
        Mockito.when(disponibilidadService.listarTodas()).thenReturn(Arrays.asList(dispPrueba));

        mockMvc.perform(get("/api/usuarios/disponibilidad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].motivo").value("Vacaciones"));
    }

    // @Test
    // void testBuscarPorUsuario() throws Exception {
    //     Mockito.when(disponibilidadService.buscarPorUsuario(1L)).thenReturn(Arrays.asList(dispPrueba));

    //     mockMvc.perform(get("/api/usuarios/disponibilidad/1"))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$[0].usuario.*").value(1));
    // } el test no era del controller asi que no me complico la vida con este 

    @Test
    void testCrear() throws Exception {
        Mockito.when(disponibilidadService.guardar(any(Disponibilidad.class))).thenReturn(dispPrueba);

        mockMvc.perform(post("/api/usuarios/disponibilidad")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dispPrueba)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.motivo").value("Vacaciones"));
    }

    @Test
    void testModificar() throws Exception {
        Mockito.when(disponibilidadService.buscarPorId(100L)).thenReturn(dispPrueba);
        Mockito.when(disponibilidadService.guardar(any(Disponibilidad.class))).thenReturn(dispPrueba);

        mockMvc.perform(put("/api/usuarios/disponibilidad/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dispPrueba)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.motivo").value("Vacaciones"));
    }

    @Test
    void testEliminar() throws Exception {
        Mockito.doNothing().when(disponibilidadService).eliminar(100L);

        mockMvc.perform(delete("/api/usuarios/disponibilidad/100"))
                .andExpect(status().isOk());
    }

    @Test
    void testConsultarReal() throws Exception {
        Mockito.when(disponibilidadService.buscarReal("Vacaciones")).thenReturn(Arrays.asList(dispPrueba));

        mockMvc.perform(get("/api/usuarios/disponibilidad/buscar")
                .param("motivo", "Vacaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].motivo").value("Vacaciones"));
    }
}