package com.example.proyectos;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

@SpringBootApplication
public class ProyectosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProyectosApplication.class, args);
    }

    @Bean
    @ConditionalOnBean(RabbitAdmin.class)
    public ApplicationRunner initRabbit(RabbitAdmin rabbitAdmin) {
        return args -> rabbitAdmin.initialize();
    }
}