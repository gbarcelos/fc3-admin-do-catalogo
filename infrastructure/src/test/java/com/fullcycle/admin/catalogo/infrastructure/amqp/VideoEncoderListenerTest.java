package com.fullcycle.admin.catalogo.infrastructure.amqp;

import static com.fullcycle.admin.catalogo.domain.utils.IdUtils.uuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import com.fullcycle.admin.catalogo.AmqpTest;
import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusCommand;
import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.admin.catalogo.domain.video.MediaStatus;
import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoEncoderCompleted;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoEncoderError;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoMessage;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoMetadata;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@AmqpTest
public class VideoEncoderListenerTest {

  @Autowired private TestRabbitTemplate rabbitTemplate;

  @Autowired private RabbitListenerTestHarness harness;

  @MockBean private UpdateMediaStatusUseCase updateMediaStatusUseCase;

  @Autowired @VideoEncodedQueue private QueueProperties queueProperties;

  @Test
  public void givenErrorResult_whenCallsListener_shouldProcess() throws InterruptedException {
    // given
    final var expectedError =
        new VideoEncoderError(new VideoMessage("123", "abc"), "Video not found");

    final var expectedMessage = Json.writeValueAsString(expectedError);

    // when
    this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

    // then
    final var invocationData =
        harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

    assertNotNull(invocationData);
    assertNotNull(invocationData.getArguments());

    final var actualMessage = (String) invocationData.getArguments()[0];
    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  public void givenCompletedResult_whenCallsListener_shouldCallUseCase()
      throws InterruptedException {
    // given
    final var expectedId = uuid();
    final var expectedOutputBucket = "codeeducationtest";
    final var expectedStatus = MediaStatus.COMPLETED;
    final var expectedEncoderVideoFolder = "anyfolder";
    final var expectedResourceId = uuid();
    final var expectedFilePath = "any.mp4";
    final var expectedMetadata =
        new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

    final var aResult =
        new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedMetadata);

    final var expectedMessage = Json.writeValueAsString(aResult);

    doNothing().when(updateMediaStatusUseCase).execute(any());

    // when
    this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

    // then
    final var invocationData =
        harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

    assertNotNull(invocationData);
    assertNotNull(invocationData.getArguments());

    final var actualMessage = (String) invocationData.getArguments()[0];
    assertEquals(expectedMessage, actualMessage);

    final var cmdCaptor = ArgumentCaptor.forClass(UpdateMediaStatusCommand.class);
    verify(updateMediaStatusUseCase).execute(cmdCaptor.capture());

    final var actualCommand = cmdCaptor.getValue();
    assertEquals(expectedStatus, actualCommand.status());
    assertEquals(expectedId, actualCommand.videoId());
    assertEquals(expectedResourceId, actualCommand.resourceId());
    assertEquals(expectedEncoderVideoFolder, actualCommand.folder());
    assertEquals(expectedFilePath, actualCommand.filename());
  }
}
