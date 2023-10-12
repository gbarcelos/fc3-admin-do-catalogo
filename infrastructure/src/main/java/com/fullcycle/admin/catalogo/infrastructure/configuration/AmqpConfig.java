package com.fullcycle.admin.catalogo.infrastructure.configuration;

import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoEvents;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

  @Bean
  @ConfigurationProperties("amqp.queues.video-created")
  @VideoCreatedQueue
  public QueueProperties videoCreatedQueueProperties() {
    return new QueueProperties();
  }

  @Bean
  @ConfigurationProperties("amqp.queues.video-encoded")
  @VideoEncodedQueue
  public QueueProperties videoEncodedQueueProperties() {
    return new QueueProperties();
  }

  @Configuration
  static class Admin {

    @Bean
    @VideoEvents
    public Exchange videoEventsExchange(@VideoCreatedQueue QueueProperties props) {
      return new DirectExchange(props.getExchange());
    }

    @Bean
    @VideoCreatedQueue
    public Queue videoCreatedQueue(@VideoCreatedQueue QueueProperties props) {
      return new Queue(props.getQueue());
    }

    @Bean
    @VideoCreatedQueue
    public Binding videoCreatedQueueBinding(
        @VideoEvents DirectExchange exchange,
        @VideoCreatedQueue Queue queue,
        @VideoCreatedQueue QueueProperties props) {
      return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
    }

    @Bean
    @VideoEncodedQueue
    public Queue videoEncodedQueue(@VideoEncodedQueue QueueProperties props) {
      return new Queue(props.getQueue());
    }

    @Bean
    @VideoEncodedQueue
    public Binding videoEncodedQueueBinding(
        @VideoEvents DirectExchange exchange,
        @VideoEncodedQueue Queue queue,
        @VideoEncodedQueue QueueProperties props) {
      return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
    }
  }
}
