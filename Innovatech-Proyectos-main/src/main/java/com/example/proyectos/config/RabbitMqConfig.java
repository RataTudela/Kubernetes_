package com.example.proyectos.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@EnableRabbit
@Configuration
@Profile("!test")
public class RabbitMqConfig {

    // Cola de Proyectos (publica)

    public static final String QUEUE_PROYECTOS = "proyectos.queue";

    public static final String EXCHANGE_PROYECTOS = "proyectos.exchange";

    public static final String ROUTING_KEY_PROYECTOS = "proyectos.routingKey";

    // Cola de Usuarios (escucha)

    public static final String QUEUE_USUARIOS = "usuarios.queue";

    public static final String EXCHANGE_USUARIOS = "usuarios.exchange";

    public static final String ROUTING_KEY_USUARIOS = "usuarios.routingKey";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_PROYECTOS, true);
    }

    @Bean
    public Queue queueUsuarios() {
        return new Queue(QUEUE_USUARIOS, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_PROYECTOS);
    }

    @Bean
    public TopicExchange exchangeUsuarios() {
        return new TopicExchange(EXCHANGE_USUARIOS);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(ROUTING_KEY_PROYECTOS);
    }

    @Bean
    public Binding bindingUsuarios(
            Queue queueUsuarios,
            TopicExchange exchangeUsuarios
    ) {
        return BindingBuilder.bind(queueUsuarios)
                .to(exchangeUsuarios)
                .with(ROUTING_KEY_USUARIOS);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @Profile("!test")
    public RabbitAdmin rabbitAdmin(
            ConnectionFactory connectionFactory
    ) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    @Profile("!test")
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}