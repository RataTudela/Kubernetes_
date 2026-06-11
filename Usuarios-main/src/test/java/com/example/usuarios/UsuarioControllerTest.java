package com.example.usuarios;

import java.util.Arrays;
import java.util.Optional;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.usuarios.controller.UsuarioController;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuarioPrueba;

    @BeforeEach
    void setUp() {
        usuarioPrueba = new Usuario();
        usuarioPrueba.setId_usuario(1L);
        usuarioPrueba.setNombre("Benjamon");
        usuarioPrueba.setEmail("benja@test.com");
        usuarioPrueba.setContraseña("123456");
        usuarioPrueba.setRol("ADMIN");
        usuarioPrueba.setEstado("ACTIVO");
    }

    @Test
    void testListar() throws Exception {
        Mockito.when(usuarioService.listarTodos()).thenReturn(Arrays.asList(usuarioPrueba));
        mockMvc.perform(get("/api/usuarios/usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Benjamon"));
    }

    @Test
    void testObtenerPorId() throws Exception {
        Mockito.when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(usuarioPrueba));
        mockMvc.perform(get("/api/usuarios/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("benja@test.com"));
    }

    @Test
    void testCrear() throws Exception {
        Mockito.when(usuarioService.guardar(any(Usuario.class))).thenReturn(usuarioPrueba);
        mockMvc.perform(post("/api/usuarios/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioPrueba)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_usuario").value(1));
    }

    @Test
    void testActualizar() throws Exception {
        Mockito.when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(usuarioPrueba));
        Mockito.when(usuarioService.guardar(any(Usuario.class))).thenReturn(usuarioPrueba);

        mockMvc.perform(put("/api/usuarios/usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioPrueba)))
                .andExpect(status().isOk());
    }

    @Test
    void testModificarRol() throws Exception {
        Mockito.when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(usuarioPrueba));
        Mockito.when(usuarioService.guardar(any(Usuario.class))).thenReturn(usuarioPrueba);

        mockMvc.perform(patch("/api/usuarios/usuario/1/rol")
                .contentType(MediaType.APPLICATION_JSON)
                .content("EDITOR"))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminar() throws Exception {
        Mockito.doNothing().when(usuarioService).eliminar(1L);
        mockMvc.perform(delete("/api/usuarios/usuario/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testLogin() throws Exception {
        Mockito.when(usuarioService.autenticar("benja@test.com", "123456"))
                .thenReturn(Optional.of(usuarioPrueba));

        mockMvc.perform(post("/api/usuarios/usuario/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioPrueba)))
                .andExpect(status().isOk());
    }
}