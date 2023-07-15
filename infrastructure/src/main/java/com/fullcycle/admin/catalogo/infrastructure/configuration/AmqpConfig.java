package com.fullcycle.admin.catalogo.infrastructure.configuration;

import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    @ConfigurationProperties("amqp.queues.video-created")
    public QueueProperties videoCreatedQueueProperties() {
        return new QueueProperties();
    }

    @Bean
    @ConfigurationProperties("amqp.queues.video-encoded")
    public QueueProperties videoEncodedQueueProperties() {
        return new QueueProperties();
    }
}
