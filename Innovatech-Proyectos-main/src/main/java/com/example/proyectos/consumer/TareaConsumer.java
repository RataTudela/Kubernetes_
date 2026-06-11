package com.example.proyectos.consumer;


import java.util.Map;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


import com.example.proyectos.model.Tarea;

import com.example.proyectos.repository.TareaRepository;


import lombok.RequiredArgsConstructor;


@Component
@Profile("!test")
@RequiredArgsConstructor

public class TareaConsumer {


  private final TareaRepository tareaRepository;


  @RabbitListener(queues = "usuarios.queue")

  public void actualizarEstadoTarea(Map<String, Object> mensaje) {

    Long idTarea = Long.valueOf(mensaje.get("idTarea").toString());

    String estado = mensaje.get("estado").toString();


    Tarea tarea = tareaRepository.findById(idTarea).orElse(null);

    if (tarea != null) {

      tarea.setEstado(estado);

      tareaRepository.save(tarea);

      System.out.println("✅ Tarea " + idTarea + " actualizada a: " + estado);

    }

  }

}