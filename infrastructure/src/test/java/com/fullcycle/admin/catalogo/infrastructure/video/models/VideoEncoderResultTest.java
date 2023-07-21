package com.fullcycle.admin.catalogo.infrastructure.video.models;

import static com.fullcycle.admin.catalogo.domain.utils.IdUtils.uuid;
import static org.assertj.core.api.Assertions.assertThat;

import com.fullcycle.admin.catalogo.JacksonTest;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class VideoEncoderResultTest {

  @Autowired private JacksonTester<VideoEncoderResult> json;

  @Test
  public void testUnmarshallSuccessResult() throws IOException {
    // given
    final var expectedId = uuid();
    final var expectedOutputBucket = "codeeducationtest";
    final var expectedStatus = "COMPLETED";
    final var expectedEncoderVideoFolder = "anyfolder";
    final var expectedResourceId = uuid();
    final var expectedFilePath = "any.mp4";
    final var expectedMetadata =
        new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

    final var json =
        """
                    {
                      "status": "%s",
                      "id": "%s",
                      "output_bucket_path": "%s",
                      "video": {
                        "encoded_video_folder": "%s",
                        "resource_id": "%s",
                        "file_path": "%s"
                      }
                    }
                """
            .formatted(
                expectedStatus,
                expectedId,
                expectedOutputBucket,
                expectedEncoderVideoFolder,
                expectedResourceId,
                expectedFilePath);

    // when
    final var actualResult = this.json.parse(json);

    assertThat(actualResult)
        .isInstanceOf(VideoEncoderCompleted.class)
        .hasFieldOrPropertyWithValue("id", expectedId)
        .hasFieldOrPropertyWithValue("outputBucket", expectedOutputBucket)
        .hasFieldOrPropertyWithValue("video", expectedMetadata);
  }

  @Test
  public void testMarshallSuccessResult() throws IOException {
    // given
    final var expectedId = uuid();
    final var expectedOutputBucket = "codeeducationtest";
    final var expectedStatus = "COMPLETED";
    final var expectedEncoderVideoFolder = "anyfolder";
    final var expectedResourceId = uuid();
    final var expectedFilePath = "any.mp4";
    final var expectedMetadata =
        new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

    final var aResult =
        new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedMetadata);

    // when
    final var actualResult = this.json.write(aResult);

    assertThat(actualResult)
        .hasJsonPathValue("$.id", expectedId)
        .hasJsonPathValue("$.output_bucket_path", expectedOutputBucket)
        .hasJsonPathValue("$.status", expectedStatus)
        .hasJsonPathValue("$.video.encoded_video_folder", expectedEncoderVideoFolder)
        .hasJsonPathValue("$.video.resource_id", expectedResourceId)
        .hasJsonPathValue("$.video.file_path", expectedFilePath);
  }

  @Test
  public void testUnmarshallErrorResult() throws IOException {
    // given
    final var expectedMessage = "Resource not found";
    final var expectedStatus = "ERROR";
    final var expectedResourceId = uuid();
    final var expectedFilePath = "any.mp4";
    final var expectedVideoMessage = new VideoMessage(expectedResourceId, expectedFilePath);

    final var json =
        """
                    {
                      "status": "%s",
                      "error": "%s",
                      "message": {
                        "resource_id": "%s",
                        "file_path": "%s"
                      }
                    }
                """
            .formatted(expectedStatus, expectedMessage, expectedResourceId, expectedFilePath);

    // when
    final var actualResult = this.json.parse(json);

    assertThat(actualResult)
        .isInstanceOf(VideoEncoderError.class)
        .hasFieldOrPropertyWithValue("error", expectedMessage)
        .hasFieldOrPropertyWithValue("message", expectedVideoMessage);
  }

  @Test
  public void testMarshallErrorResult() throws IOException {
    // given
    final var expectedMessage = "Resource not found";
    final var expectedStatus = "ERROR";
    final var expectedResourceId = uuid();
    final var expectedFilePath = "any.mp4";
    final var expectedVideoMessage = new VideoMessage(expectedResourceId, expectedFilePath);

    final var aResult = new VideoEncoderError(expectedVideoMessage, expectedMessage);

    // when
    final var actualResult = this.json.write(aResult);

    assertThat(actualResult)
        .hasJsonPathValue("$.status", expectedStatus)
        .hasJsonPathValue("$.error", expectedMessage)
        .hasJsonPathValue("$.message.resource_id", expectedResourceId)
        .hasJsonPathValue("$.message.file_path", expectedFilePath);
  }
}
