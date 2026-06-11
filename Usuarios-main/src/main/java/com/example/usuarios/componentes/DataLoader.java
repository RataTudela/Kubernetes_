package com.example.usuarios.componentes;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.usuarios.model.CargaTrabajo;
import com.example.usuarios.model.Disponibilidad;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.CargaTrabajoRepository;
import com.example.usuarios.repository.DisponibilidadRepository;
import com.example.usuarios.repository.UsuarioRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CargaTrabajoRepository cargaRepository;
    private final DisponibilidadRepository disponibilidadRepository;

    public DataLoader(UsuarioRepository usuarioRepository, 
                      CargaTrabajoRepository cargaRepository, 
                      DisponibilidadRepository disponibilidadRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cargaRepository = cargaRepository;
        this.disponibilidadRepository = disponibilidadRepository;
    }

    @Override
    @Transactional 
    public void run(String... args) throws Exception {
        
        System.out.println(">> Verificando datos en Neon...");

        String emailJuan = "juan.perez@innovatech.com";
        if (usuarioRepository.findByEmail(emailJuan).isEmpty()) {
            Usuario juan = new Usuario();
            juan.setNombre("Juan Perez");
            juan.setEmail(emailJuan);
            juan.setContraseña("Informatica.25");
            juan.setRol("CONSULTOR");
            juan.setEstado("ACTIVO");
            usuarioRepository.save(juan);

            CargaTrabajo cargaJuan = new CargaTrabajo();
            cargaJuan.setUsuario(juan);
            cargaJuan.setHoras_asignadas(25);
            cargaJuan.setNombreTarea("Mantenimiento de Servidores (Infraestructura)");
            cargaRepository.save(cargaJuan);
            
            System.out.println(">> Usuario Juan creado con tarea asignada.");
        }

        String emailMaria = "maria.garcia@innovatech.com";
        Optional<Usuario> mariaOpt = usuarioRepository.findByEmail(emailMaria);
        
        if (mariaOpt.isEmpty()) {
            Usuario maria = new Usuario();
            maria.setNombre("Maria Garcia");
            maria.setEmail(emailMaria);
            maria.setContraseña("Informatica.25");
            maria.setRol("SENIOR");
            maria.setEstado("ACTIVO");
            usuarioRepository.save(maria);

            CargaTrabajo cargaMaria = new CargaTrabajo();
            cargaMaria.setUsuario(maria);
            cargaMaria.setHoras_asignadas(35);
            cargaMaria.setNombreTarea("Desarrollo Frontend - Dashboard Clientes");
            cargaRepository.save(cargaMaria);

            Disponibilidad vacacionesMaria = new Disponibilidad();
            vacacionesMaria.setUsuario(maria);
            vacacionesMaria.setFechaInicio(LocalDate.of(2026, 5, 1));
            vacacionesMaria.setFechaFin(LocalDate.of(2026, 5, 15));
            vacacionesMaria.setMotivo("Vacaciones anuales");
            disponibilidadRepository.save(vacacionesMaria);
            
            System.out.println(">> Usuario Maria creado con tarea y disponibilidad.");
        }

    }
}