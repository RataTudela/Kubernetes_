package com.example.usuarios;

import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.usuarios.service.UsuarioService;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) 
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository; 

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioFicticio;

    @BeforeEach
    void setUp() {
        usuarioFicticio = new Usuario();
        usuarioFicticio.setEmail("benja@test.com");
        usuarioFicticio.setContraseña("secreto123");
    }

    @Test
    void testAutenticar_Exito() {
        Mockito.when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuarioFicticio));

        Optional<Usuario> resultado = usuarioService.autenticar("benja@test.com", "secreto123");

        assertTrue(resultado.isPresent());
        assertEquals("benja@test.com", resultado.get().getEmail());
    }
    @Test
    void testListarTodos() {
        Mockito.when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuarioFicticio));

        java.util.List<Usuario> lista = usuarioService.listarTodos();

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("benja@test.com", lista.get(0).getEmail());
    }

    @Test
    void testObtenerPorId() {
        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioFicticio));

        Optional<Usuario> resultado = usuarioService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("benja@test.com", resultado.get().getEmail());
    }

    @Test
    void testEliminar() {
        Mockito.doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.eliminar(1L);

        Mockito.verify(usuarioRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testAutenticar_PasswordIncorrecto() {
        Mockito.when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuarioFicticio));

        Optional<Usuario> resultado = usuarioService.autenticar("benja@test.com", "clave_erronea");

        assertFalse(resultado.isPresent());
    }

    @Test
    void testGuardarUsuario() {
        Mockito.when(usuarioRepository.save(usuarioFicticio)).thenReturn(usuarioFicticio);

        Usuario guardado = usuarioService.guardar(usuarioFicticio);

        assertNotNull(guardado);
        Mockito.verify(usuarioRepository, Mockito.times(1)).save(usuarioFicticio);
    }
}