package com.fullcycle.admin.catalogo.infrastructure.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fullcycle.admin.catalogo.AmqpTest;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaCreated;
import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.services.EventService;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@AmqpTest
public class RabbitEventServiceTest {

  private static final String LISTENER = "video.created";

  @Autowired @VideoCreatedQueue private EventService publisher;

  @Autowired private RabbitListenerTestHarness harness;

  @Test
  public void shouldSendMessage() throws InterruptedException {
    // given
    final var notification = new VideoMediaCreated("resource", "filepath");

    final var expectedMessage = Json.writeValueAsString(notification);

    // when
    this.publisher.send(notification);

    // then
    final var invocationData = harness.getNextInvocationDataFor(LISTENER, 1, TimeUnit.SECONDS);

    assertNotNull(invocationData);
    assertNotNull(invocationData.getArguments());

    final var actualMessage = (String) invocationData.getArguments()[0];

    assertEquals(expectedMessage, actualMessage);
  }

  @Component
  static class VideoCreatedNewsListener {
    @RabbitListener(id = LISTENER, queues = "${amqp.queues.video-created.routing-key}")
    void onVideoCreated(@Payload String message) {
      System.out.println(message);
    }
  }
}
